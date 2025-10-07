import os
import base64
import argparse
from pathlib import Path
import yaml
import requests

"""
Push ADO test cases defined in outputs/ado_testcases.yaml to Azure DevOps.

Env vars required for live push:
- ADO_ORG: Azure DevOps organization name (e.g., myorg)
- ADO_PROJECT: Project name (e.g., MyProject)
- ADO_PAT: Personal Access Token

Usage:
  python pipeline/ado_push.py --input outputs/ado_testcases.yaml --dry-run false
"""

def create_work_item(org: str, project: str, pat: str, title: str, area_path: str, description: str, tags: str, dry_run: bool = True):
    url = f"https://dev.azure.com/{org}/{project}/_apis/wit/workitems/$Test%20Case?api-version=7.1-preview.3"
    headers = {
        "Content-Type": "application/json-patch+json",
        "Authorization": "Basic " + base64.b64encode((":" + pat).encode()).decode(),
    }
    body = [
        {"op": "add", "path": "/fields/System.Title", "value": title},
        {"op": "add", "path": "/fields/System.AreaPath", "value": area_path},
        {"op": "add", "path": "/fields/System.Tags", "value": tags},
        # For simplicity, we store steps in description; mapping to Microsoft.VSTS.TCM.Steps XML can be added later
        {"op": "add", "path": "/fields/System.Description", "value": description},
    ]
    if dry_run:
        return {"dry_run": True, "url": url, "body": body}
    resp = requests.patch(url, json=body, headers=headers)
    resp.raise_for_status()
    return resp.json()

def main():
    parser = argparse.ArgumentParser()
    parser.add_argument("--input", default=str(Path("outputs/ado_testcases.yaml").resolve()))
    parser.add_argument("--dry-run", default="true", choices=["true", "false"], help="Do not push, just show payload")
    args = parser.parse_args()

    data = yaml.safe_load(Path(args.input).read_text(encoding="utf-8"))
    org = os.getenv("ADO_ORG")
    project = os.getenv("ADO_PROJECT")
    pat = os.getenv("ADO_PAT")
    dry_run = args["dry_run"].lower() == "true" if isinstance(args, dict) else args.dry_run.lower() == "true"

    results = []
    for tc in data.get("ado_testcases", []):
        title = tc.get("title", "Generated Test")
        area_path = tc.get("area_path", project or "")
        tags = ",".join(tc.get("tags", []))
        steps = tc.get("steps", [])
        description_lines = [f"Step: {s.get('step')} | Expected: {s.get('expected')}" for s in steps]
        description = "\n".join(description_lines)
        result = create_work_item(org or "", project or "", pat or "", title, area_path, description, tags, dry_run=dry_run)
        results.append(result)

    print("Results:")
    for r in results:
        print(r)

if __name__ == "__main__":
    main()