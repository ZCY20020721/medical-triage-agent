"""意图路由：简单直达 / 复杂多步 / 高风险 / 工具 / 信息不足。"""

TOOL_KEYWORDS = ("挂号", "号", "排班", "预约")


def route(
    symptom: str,
    symptoms: list[str],
    age_group: str | None,
    duration: str | None,
    companions: list[str],
    red_flag: bool,
) -> str:
    if red_flag:
        return "red_flag"
    # 工具意图要先于「无症状」判断：如「帮我挂个号」无症状实体但属工具请求
    if any(k in symptom for k in TOOL_KEYWORDS):
        return "tool"
    if not symptoms:
        return "insufficient"
    if len(symptoms) >= 2 or age_group or duration or companions:
        return "complex"
    return "simple"
