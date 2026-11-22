"""图谱查询：直接链路（症状→科室）+ 多跳链路（症状→疾病→科室，疾病不外露）。"""
from collections import Counter
from .data import SYMPTOM_TO_DEPT, DISEASES


def direct_triage(symptoms: list[str]) -> list[tuple[str, float]]:
    """直接链路：高频症状 → 科室，返回 (科室, 命中分)。"""
    counter: Counter[str] = Counter()
    for s in symptoms:
        for d in SYMPTOM_TO_DEPT.get(s, []):
            counter[d] += 1
    total = len(symptoms) or 1
    return [(d, round(c / total, 2)) for d, c in counter.most_common()]


def multi_hop(symptoms: list[str]) -> list[tuple[str, float]]:
    """多跳链路：症状 → 疾病(内部) → 科室，返回 (科室, 命中分)，疾病不外露。"""
    dept_hits: Counter[str] = Counter()
    for disease, meta in DISEASES.items():
        overlap = set(symptoms) & set(meta["symptoms"])
        if overlap:
            dept_hits[meta["dept"]] += len(overlap)
    if not dept_hits:
        return []
    return [(d, round(c, 2)) for d, c in dept_hits.most_common()]
