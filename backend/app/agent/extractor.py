"""实体抽取：症状 / 人群 / 时长 / 伴随症状 + 高风险识别。"""
import re
from ..kg.data import SYMPTOM_ALIASES, RED_FLAG_RULES


def extract_symptoms(text: str) -> list[str]:
    """按词典 + 别名抽取症状实体，返回标准症状名列表。"""
    found: list[str] = []
    for std, aliases in SYMPTOM_ALIASES.items():
        if std in text or any(a in text for a in aliases):
            found.append(std)
    return found


def extract_age_group(text: str) -> str | None:
    if any(w in text for w in ["老人", "奶奶", "爷爷", "姥姥", "姥爷", "长辈"]):
        return "老人"
    if any(w in text for w in ["孩子", "小孩", "宝宝", "儿童", "儿子", "女儿"]):
        return "儿童"
    if any(w in text for w in ["孕妇", "怀孕", "孕期"]):
        return "孕妇"
    return None


def extract_duration(text: str) -> str | None:
    m = re.search(r"(\d+)\s*(天|小时|周|月)", text)
    return m.group(0) if m else None


def check_red_flag(text: str) -> tuple[bool, str | None]:
    """高风险识别：关键词组合命中即触发（漏报为安全底线）。"""
    for keywords, hint in RED_FLAG_RULES:
        if all(k in text for k in keywords):
            return True, hint
    return False, None
