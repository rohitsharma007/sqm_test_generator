# POC 1.0 – SQM Test Generation Pipeline

This project demonstrates a data ingestion and test generation pipeline:

- Java SQM libraries for Web and API with 10 reusable methods each
- ADO test case template (YAML) for non-linear, step-based test cases
- Vector database (FAISS) storing embeddings of the Java libraries and templates
- Retriever + Prompt + LLM chain to generate test cases and TestNG-style scripts using SQM libraries

## Prerequisites

- macOS with Python 3.9+
- Java 17+
- Optional: [Ollama](https://ollama.com/) running a local model like `llama3` or `qwen2`

## Setup

```bash
python3 -m venv .venv
source .venv/bin/activate
pip install -r requirements.txt
```

If using Ollama, ensure a model is pulled:

```bash
ollama pull llama3
```

## Usage

1. Run ingestion to build FAISS index from Java libs and templates:
```bash
python pipeline/ingest.py
```

2. Generate from inputs (acceptance criteria, swagger snippets, etc.):
```bash
# Run both agents
python pipeline/generate.py --input examples/inputs/acceptance_criteria.md --llm ollama --model llama3 --agent both

# Run TestcaseAgent only (produces ado_testcases.yaml)
python pipeline/generate.py --input examples/inputs/acceptance_criteria.md --llm ollama --model llama3 --agent testcase

# Run ScriptAgent only (produces TestSuiteGenerated.java)
python pipeline/generate.py --input examples/inputs/acceptance_criteria.md --llm ollama --model llama3 --agent script
```

Outputs will be saved to `outputs/` including:

- `ado_testcases.yaml` – ADO-style test case artifacts
- `TestSuiteGenerated.java` – TestNG-style suite using SQM libraries

### Quickstart: Execution Commands

```bash
# 1) Setup and activate environment
python3 -m venv .venv && source .venv/bin/activate && pip install -r requirements.txt

# 2) Build embeddings index
python pipeline/ingest.py

# 3a) Generate with local LLM (Ollama)
python pipeline/generate.py --input examples/inputs/acceptance_criteria.md --llm ollama --model llama3 --agent both

# 3b) Generate using deterministic fallback (no external LLM)
python pipeline/generate.py --input examples/inputs/acceptance_criteria.md --llm hf --model tiny --agent both

# 4) View outputs
ls -la outputs
sed -n '1,120p' outputs/ado_testcases.yaml
sed -n '1,120p' outputs/TestSuiteGenerated.java
```

## Structure

```
java/
  sqm-api/src/main/java/com/example/sqm/api/ApiClient.java
  sqm-web/src/main/java/com/example/sqm/web/WebClient.java
templates/
  ado_testcase_template.yaml
pipeline/
  ingest.py
  generate.py
examples/inputs/
  acceptance_criteria.md
outputs/
```

## Notes

- Embeddings use `FastEmbedEmbeddings` for lightweight, fast local embedding.
- FAISS index lives in `.vector_store/faiss_index/`.
- LLM can be local Ollama (`--llm ollama --model <name>`) or a deterministic fallback (`--llm hf`) that formats ADO YAML and Java without external inference.

## Java: Build and Run Tests

```bash
# From repository root
cd java
mvn -q -DskipTests=false test
```

This compiles `sqm-api` and `sqm-web`, and runs tests in `sqm-suite`. The generated `outputs/TestSuiteGenerated.java` is copied into the suite at build time and executed by TestNG.

## Azure DevOps Push (ADO)

Set environment variables:

```bash
export ADO_ORG="<your-org>"
export ADO_PROJECT="<your-project>"
export ADO_PAT="<your-personal-access-token>"
```

Dry run (show payloads without pushing):

```bash
python pipeline/ado_push.py --input outputs/ado_testcases.yaml --dry-run true
```

Live push:

```bash
python pipeline/ado_push.py --input outputs/ado_testcases.yaml --dry-run false
```

Note: Mapping steps to `Microsoft.VSTS.TCM.Steps` XML can be added to create structured step fields; currently steps are included in `System.Description`.
