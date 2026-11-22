"""FastAPI 入口：医导 · 云端导诊 Agent 服务。"""
import os

from fastapi import FastAPI
from fastapi.middleware.cors import CORSMiddleware
from fastapi.responses import FileResponse

from .config import get_settings
from .schemas import TriageRequest, TriageResponse
from .pipeline import run_triage
from .llm.client import LLMClient

_settings = get_settings()
_llm = LLMClient(_settings)

_BASE_DIR = os.path.dirname(os.path.dirname(os.path.abspath(__file__)))

app = FastAPI(title="医导 · 云端导诊 Agent", version="0.1.0")

app.add_middleware(
    CORSMiddleware,
    allow_origins=["*"],
    allow_methods=["*"],
    allow_headers=["*"],
)


@app.get("/health")
async def health():
    return {"status": "ok", "llm_enabled": _llm.enabled, "provider": _settings.llm_provider}


@app.get("/", include_in_schema=False)
async def index():
    return FileResponse(os.path.join(_BASE_DIR, "app", "static", "index.html"))


@app.post("/api/v1/triage", response_model=TriageResponse)
async def triage(req: TriageRequest):
    return await run_triage(req, _llm)
