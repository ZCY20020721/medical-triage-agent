"""导诊流水线：抽取 → 路由 → 规划 → 兜底 → 生成指引 → OutputGuard。

LLM 优先（能云端尽量云端），未配置或失败时自动降级到模板/词典，保证服务始终可运行。
"""
from .schemas import TriageRequest, TriageResponse, Source, ReasoningStep
from .agent import extractor, router, planner, guard
from .llm.client import LLMClient


def _template_guidance(departments: list[str]) -> str:
    if not departments:
        return "信息不足，建议补充症状描述或前往医院导诊台咨询。"
    return f"建议前往{'、'.join(departments)}就诊，可携带身份证、医保卡及相关检查资料。"


async def run_triage(req: TriageRequest, llm: LLMClient) -> TriageResponse:
    # 人群：请求字段 > 文本抽取 > 老人模式默认老人
    age_group = req.age_group or extractor.extract_age_group(req.symptom) or (
        "老人" if req.elderly_mode else None
    )
    duration = req.duration or extractor.extract_duration(req.symptom)

    symptoms = extractor.extract_symptoms(req.symptom)
    red_flag, hint = extractor.check_red_flag(req.symptom)

    if red_flag:
        return TriageResponse(
            intent="red_flag",
            red_flag=True,
            red_flag_hint=hint,
            guidance=hint or "",
            confidence=1.0,
            elderly_mode=req.elderly_mode,
        )

    intent = router.route(req.symptom, symptoms, age_group, duration, req.companions, red_flag=False)

    if intent == "insufficient":
        return TriageResponse(
            intent="insufficient",
            needs_clarification=True,
            clarification_questions=["请再描述一下症状（如：哪里不舒服、持续多久）"],
            guidance="请再描述一下症状，以便判断该挂哪个科。",
            confidence=0.0,
            elderly_mode=req.elderly_mode,
        )

    if intent == "tool":
        departments = planner.plan(symptoms, age_group, "simple")[0]
        return TriageResponse(
            intent="tool",
            departments=departments,
            guidance=(
                "挂号为演示功能（mock），即将跳转到挂号页面。"
                if departments
                else "请先描述症状或选择要挂号的科室。"
            ),
            confidence=0.8,
            elderly_mode=req.elderly_mode,
            reasoning_steps=[ReasoningStep(step="识别到挂号/号源需求")],
        )

    departments, confidence, steps = planner.plan(symptoms, age_group, intent)

    guidance = _template_guidance(departments)
    llm_guidance = await llm.generate_guidance(departments, symptoms)
    if llm_guidance:
        guidance = llm_guidance
    guidance = guard.sanitize(guidance)

    sources = [Source(title=f"导诊知识库 · {'/'.join(symptoms)}")]

    return TriageResponse(
        intent=intent,
        departments=departments,
        guidance=guidance,
        confidence=confidence,
        sources=sources,
        reasoning_steps=steps,
        elderly_mode=req.elderly_mode,
    )
