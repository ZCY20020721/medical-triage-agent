"""OpenAI 兼容 LLM 客户端（DeepSeek / GLM 均可），可选增强。"""
import httpx
from ..config import Settings


class LLMClient:
    def __init__(self, settings: Settings):
        self.settings = settings
        self.enabled = settings.llm_enabled

    def _headers(self) -> dict:
        return {"Authorization": f"Bearer {self.settings.llm_api_key}"}

    async def chat(self, messages: list[dict], max_tokens: int = 512) -> str:
        if not self.enabled:
            return ""
        url = self.settings.llm_base_url.rstrip("/") + "/chat/completions"
        payload = {
            "model": self.settings.llm_model,
            "messages": messages,
            "max_tokens": max_tokens,
            "temperature": 0.2,
            "stream": False,
        }
        async with httpx.AsyncClient(timeout=30) as client:
            resp = await client.post(url, headers=self._headers(), json=payload)
            resp.raise_for_status()
            data = resp.json()
        return data["choices"][0]["message"]["content"].strip()

    async def generate_guidance(self, departments: list[str], symptoms: list[str]) -> str:
        """用 LLM 生成自然语言就医指引；失败/未启用时返回空串。"""
        if not self.enabled:
            return ""
        try:
            user = (
                f"症状：{'、'.join(symptoms)}；建议就诊科室：{'、'.join(departments)}。"
                "请生成一句不超过 40 字的就医指引，不得出现任何疾病名称或诊断结论。"
            )
            return await self.chat([{"role": "user", "content": user}], max_tokens=80)
        except Exception:
            return ""
