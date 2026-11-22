"""配置：从环境变量 / .env 读取 LLM 相关设置。"""
import os
from dataclasses import dataclass

try:
    from dotenv import load_dotenv
    load_dotenv()
except Exception:
    pass


@dataclass
class Settings:
    llm_provider: str = "none"  # deepseek | glm | none
    llm_api_key: str = ""
    llm_base_url: str = "https://api.deepseek.com"
    llm_model: str = "deepseek-chat"

    @property
    def llm_enabled(self) -> bool:
        return self.llm_provider in ("deepseek", "glm") and bool(self.llm_api_key)


def get_settings() -> Settings:
    return Settings(
        llm_provider=os.getenv("LLM_PROVIDER", "none").strip().lower(),
        llm_api_key=os.getenv("LLM_API_KEY", "").strip(),
        llm_base_url=os.getenv("LLM_BASE_URL", "https://api.deepseek.com").strip(),
        llm_model=os.getenv("LLM_MODEL", "deepseek-chat").strip(),
    )
