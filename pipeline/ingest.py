import os
import yaml
from pathlib import Path
from langchain_community.vectorstores import FAISS
from langchain_community.embeddings.fastembed import FastEmbedEmbeddings
from langchain.text_splitter import RecursiveCharacterTextSplitter
from langchain.docstore.document import Document

BASE = Path(__file__).resolve().parents[1]
SRC_FILES = [
    BASE / "java/sqm-api/src/main/java/com/example/sqm/api/ApiClient.java",
    BASE / "java/sqm-web/src/main/java/com/example/sqm/web/WebClient.java",
    BASE / "templates/ado_testcase_template.yaml",
]

def load_documents():
    docs = []
    for fp in SRC_FILES:
        if not fp.exists():
            continue
        text = fp.read_text(encoding="utf-8")
        docs.append(Document(page_content=text, metadata={"source": str(fp)}))
    return docs

def main():
    splitter = RecursiveCharacterTextSplitter(chunk_size=800, chunk_overlap=100)
    docs = load_documents()
    chunks = splitter.split_documents(docs)

    embeddings = FastEmbedEmbeddings()
    store_dir = BASE / ".vector_store/faiss_index"
    store_dir.mkdir(parents=True, exist_ok=True)
    db = FAISS.from_documents(chunks, embeddings)
    db.save_local(str(store_dir))
    print(f"Saved FAISS index to {store_dir}")

if __name__ == "__main__":
    main()