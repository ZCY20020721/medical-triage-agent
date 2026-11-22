"""OutputGuard：合规红线——任何输出不得含疾病名（诊断结论）。"""
from ..kg.data import DISEASE_NAMES


def contains_disease(text: str) -> bool:
    return any(d in text for d in DISEASE_NAMES)


def sanitize(text: str) -> str:
    """把疾病名替换为占位符，兜底拦截（正常情况 LLM/模板不该产出疾病名）。"""
    for d in sorted(DISEASE_NAMES, key=len, reverse=True):
        if d in text:
            text = text.replace(d, "××")
    return text


def assert_clean(*texts: str) -> bool:
    return not any(contains_disease(t) for t in texts if t)
