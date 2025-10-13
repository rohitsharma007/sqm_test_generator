# Low-Level Design (LLD)

## 1. Introduction
- Purpose: Document the detailed technical design for the test automation and knowledge pipeline system so stakeholders understand components, interactions, deployment, and operational needs.
- Scope: Covers Java Selenium test suite, shared web client utilities, Python pipeline for ingestion/generation, vector index, optional Azure DevOps integration, and outputs.
- Goals: Reliable test planning/execution, scalable ingestion and enrichment, traceable knowledge management, and simple operations.

## 2. Overall Architecture

### 2.1 Layers
- Presentation & Control: CLI/Dev tools to run pipeline and tests (`pipeline/*.py`, Maven/TestNG). Provides commands to ingest, generate, and execute.
- Application Services: 
  - Test Suite (`java/sqm-suite`): TestNG test cases orchestrating flows.
  - Web Client (`java/sqm-web`): Selenium helpers encapsulating browser actions.
  - API (stub) (`java/sqm-api`): placeholder for service APIs if needed.
- Data & Knowledge: Vector store (`.vector_store/faiss_index`), outputs (`outputs/*.yaml`), templates (`templates/*.yaml`), examples (`examples/inputs/*`).
- Integration: Azure DevOps (ADO) for test case sourcing via REST APIs; optional cloud storage for artifacts.

### 2.2 Deployment View
- Local Developer Machine (macOS):
  - Python 3.10+ with `requirements.txt` dependencies.
  - Java 17+, Maven 3.8+; Chrome + WebDriverManager downloads drivers automatically.
  - File-system persists vector indices and outputs.
- CI (optional):
  - Pipeline stages: lint → unit tests → build → e2e tests → artifact publish.
  - Secrets: ADO PAT, environment configs.

### 2.3 Architecture Diagram
```mermaid
flowchart LR
    subgraph Dev[Developer/CI]
      CLI[Pipeline CLI (ingest/generate/push)]
    end

    subgraph ADO[Azure DevOps]
      WIQ[WIQL/Test Plans API]
    end

    CLI -->|Fetch cases| WIQ
    WIQ -->|Test Cases JSON/XML| PIPE[(Ingestion & Staging)]
    PIPE --> ENRICH[Enrichment Pipeline]
    ENRICH --> KG[Knowledge Graph Manager]
    ENRICH --> VEC[Vector Index Manager]
    KG --> OUT[Outputs YAML/Docs]
    VEC --> VS[FAISS Index]

    subgraph Java[Java Test Suite]
      TS[TestNG Tests]
      WC[WebClient (Selenium)]
    end

    OUT --> TS
    TS --> WC
    WC --> WEB[Target Web Apps]
```

### 2.4 Azure Components & Purpose
- Azure DevOps Services:
  - Work Items (Test Case): Source of scenarios; fields include title, tags, priority, steps (XML).
  - Test Plans & Suites: Hierarchical organization of cases; API for suites and cases.
  - Personal Access Token (PAT): Auth for REST APIs.
- Optional Azure Storage/Key Vault (future): Store artifacts and manage secrets.

## 3. Component Design

### 3.1 Connectors (Perception Agents)
- ADO Connector: HTTP client calling WIQL and Test Plans endpoints; parses JSON/XML.
- File Connector: Reads `examples/inputs/*` for curated scenarios.
- Web Perception (via Selenium): Captures DOM state via waits, screenshots, and element text for assertions.

### 3.2 Ingestion & Staging
- Inputs: ADO Work Item data, template files, curated markdown.
- Process: Normalize fields → parse test steps XML → stage into internal model → serialize to YAML using `templates/ado_testcase_template.yaml`.
- Storage: `outputs/ado_testcases.yaml` accumulates staged cases.

### 3.3 Enrichment Pipeline
- Functions: Deduplicate, tag mapping, priority normalization, scenario synthesis from acceptance criteria, and linking to example inputs.
- Tools: Python (`pipeline/generate.py`, `ingest.py`) with pluggable transforms.
- Outputs: Cleaned YAML/markdown for consumption by the test suite and stakeholders.

### 3.4 Knowledge Graph Manager
- Model: Nodes = Test Cases, Steps, Systems, Tags; Edges = depends_on, validates, covers.
- Operations: Build graph from YAML; maintain traceability to ADO IDs; export summaries.
- Store: Lightweight in-memory or file-based graph; future: Neo4j/Gremlin if needed.

### 3.5 Vector Index Manager
- Store: FAISS index under `.vector_store/faiss_index` for semantic retrieval.
- Pipeline: Ingest texts (cases, docs) → embed → index → persist.
- Query: Retrieve top-K similar items for planning and change impact analysis.

### 3.6 Retrieval & Planning
- Inputs: Query (feature, change request, failing test).
- Steps: Retrieve from vector index → consult knowledge graph → plan test selection and order → output execution plan.
- Execution Hand-off: Feed selected scenarios into Java TestNG suite.

### 3.7 Change & Impact Analysis
- Detect changes in inputs (requirements, ADO cases, code diffs).
- Map impacted areas via tags, graph edges, and embeddings.
- Produce a prioritized list of tests to run and areas to review.

### 3.8 Test Planning & Execution
- Planning: Assemble tests based on YAML and enrichment outputs.
- Execution: `sqm-suite` drives Selenium via `WebClient`; reports and screenshots stored under `target/`.
- Observability: Logs from helpers (`click`, `type`, `wait`), screenshots for evidence.

## 4. Interfaces & Data Contracts
- ADO Work Items: JSON with fields `System.Title`, `System.Tags`, `Microsoft.VSTS.Common.Priority`, `Microsoft.VSTS.TCM.Steps` (XML).
- YAML Schema (template-driven): `ado_testcases:` array of cases with `id`, `title`, `priority`, `tags`, `steps` (`action`, `expected`).
- Selenium Actions: Methods returning status strings; failures raise exceptions in tests.

## 5. Operational Requirements
- Environments: macOS dev, optional CI runner.
- Secrets: `ADO_PAT` via env var; never commit.
- Dependencies: Python `requirements.txt`, Java/Maven, Chrome.
- Build & Run:
  - Pipeline: `python pipeline/ingest.py` → `python pipeline/generate.py`.
  - Tests: `mvn -f java/pom.xml -pl sqm-suite -am test`.

## 6. Security & Compliance
- PAT scope: Minimum read scopes for Work Items and Test Management.
- Data retention: Store only necessary fields; redact sensitive info in outputs.
- Access controls: Limit who can run with live ADO creds.

## 7. Observability & Quality
- Logs: CLI and Maven outputs; explicit wait logs.
- Artifacts: Screenshots under `java/sqm-suite/target/screenshots`.
- Metrics (future): Test pass rate, flake rate, ingestion success count.

## 8. Risks & Mitigations
- Selector flakiness → Use explicit waits and resilient selectors.
- ADO schema changes → Version endpoints and validate fields.
- Embedding drift → Periodic re-indexing.

## 9. Roadmap
- CI integration with nightly ingestion and selective test runs.
- Azure Storage + Key Vault for artifacts/secrets.
- Neo4j for durable knowledge graph.

## 10. Glossary
- ADO: Azure DevOps.
- WIQL: Work Item Query Language.
- KG: Knowledge Graph.
- VEC: Vector Index.

## 11. AI Frameworks & Agents
- Frameworks:
  - LangChain: Orchestrates tools, retrievers, and agent workflows in the Python pipeline.
  - Sentence-Transformers: Provides embedding models to convert text into vectors for similarity search.
  - Transformers + Torch + Accelerate: Runtime stack enabling efficient model inference on CPU/GPU.
  - FAISS (CPU): Vector index for fast nearest-neighbor retrieval over embedded documents.
  - Ollama: Optional local LLM runtime; integrates with LangChain for enrichment or planning.
- Agents:
  - Perception/Connectors: ADO and file connectors to fetch scenarios; parse/normalize fields and steps.
  - Enrichment Agent: Summarizes, deduplicates, tags, and links scenarios; optionally uses an LLM via LangChain/Ollama.
  - Retrieval & Planning Agent: Uses FAISS embeddings (sentence-transformers) to retrieve relevant scenarios and plan execution ordering.
  - Change & Impact Analysis Agent: Maps requirement/repo changes to impacted tests using embeddings, tags, and metadata.
  - Test Execution Orchestrator: Hands selected scenarios to Java TestNG via WebClient; not an AI agent.

## 12. Requirements.txt Explanation
- `faiss-cpu`
  - Purpose: High-performance vector index for similarity search.
  - Used For: Retrieval/planning and impact analysis over embedded test cases and documents.
- `sentence-transformers`
  - Purpose: Pretrained embedding models (e.g., MiniLM) to convert text to dense vectors.
  - Used For: Indexing ADO cases, scenarios, and docs to power FAISS searches.
- `langchain`
  - Purpose: Framework to compose prompts, tools, retrievers, and agent pipelines.
  - Used For: Building enrichment and retrieval/planning flows in `pipeline/` scripts.
- `langchain-community`
  - Purpose: Community integrations for LangChain (models, vector stores, tools).
  - Used For: FAISS helpers, Ollama model wrappers, additional retriever utilities.
- `pydantic`
  - Purpose: Data models and validation.
  - Used For: Validating structured test case objects and pipeline configuration before YAML export.
- `pyyaml`
  - Purpose: YAML input/output.
  - Used For: Loading templates and writing `outputs/ado_testcases.yaml` in a consistent schema.
- `transformers`
  - Purpose: Model architectures, tokenizers, and utilities leveraged by `sentence-transformers`.
  - Used For: Embedding backend and any optional transformer-driven tasks.
- `accelerate`
  - Purpose: Efficient device management and performance optimization for transformer inference.
  - Used For: Smooth CPU/GPU execution without custom device code.
- `torch`
  - Purpose: Deep learning runtime.
  - Used For: Required by `sentence-transformers`/`transformers` to compute embeddings.
- `ollama`
  - Purpose: Client bindings for a local LLM server.
  - Used For: Optional LLM-backed enrichment/planning when running models locally; otherwise unused.
- `requests`
  - Purpose: HTTP client library.
  - Used For: ADO REST API calls (WIQL/Test Plans), and general HTTP integrations in ingestion.

Notes:
- CPU-only operation is supported out of the box (`faiss-cpu`, Torch CPU builds); GPU acceleration is optional.
- Some dependencies are transitive (e.g., `sentence-transformers` relies on `transformers` and `torch`).