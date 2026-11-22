"""多步规划：把症状/人群映射为科室建议 + 推理步骤 + 置信度。"""
from ..kg import graph
from ..schemas import ReasoningStep

# 特殊人群优先就诊科室（演示用简化规则）
PEDIATRIC_SYMPTOMS = {"发热", "咳嗽", "腹泻", "呕吐", "腹痛", "咽痛", "鼻塞"}


def _apply_cohort(departments: list[str], age_group: str | None, symptoms: list[str]) -> tuple[list[str], str | None]:
    """特殊人群科室优先：儿童 → 儿科，孕妇 → 妇产科。返回 (科室, 附加推理步骤文案或 None)。"""
    if age_group == "儿童" and any(s in PEDIATRIC_SYMPTOMS for s in symptoms):
        return ["儿科"] + [d for d in departments if d != "儿科"], "结合儿童人群，优先建议儿科"
    if age_group == "孕妇":
        return ["妇产科"] + [d for d in departments if d != "妇产科"], "结合孕妇人群，优先建议妇产科"
    return departments, None


def plan(symptoms: list[str], age_group: str | None, intent: str):
    """返回 (departments, confidence, reasoning_steps)。"""
    steps: list[ReasoningStep] = []
    steps.append(ReasoningStep(step=f"识别症状：{'、'.join(symptoms)}"))
    if age_group:
        steps.append(ReasoningStep(step=f"识别人群：{age_group}"))

    depts: list[tuple[str, float]] = graph.direct_triage(symptoms)
    if not depts:
        multi = graph.multi_hop(symptoms)
        if multi:
            depts = multi
            steps.append(ReasoningStep(step="内部推导：结合症状与人群匹配候选科室", internal=True))

    if not depts:
        return [], 0.0, steps

    departments = [d for d, _ in depts[:2]]
    departments, cohort_note = _apply_cohort(departments, age_group, symptoms)
    if cohort_note:
        steps.append(ReasoningStep(step=cohort_note))

    top_score = depts[0][1]
    if intent == "simple" and len(symptoms) == 1:
        confidence = min(0.95, 0.6 + top_score * 0.4)
    else:
        confidence = min(0.85, 0.4 + top_score * 0.5)

    steps.append(ReasoningStep(step=f"确定建议科室：{'、'.join(departments)}"))
    return departments, round(confidence, 2), steps
