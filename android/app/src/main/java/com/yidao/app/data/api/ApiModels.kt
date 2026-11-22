package com.yidao.app.data.api

import com.google.gson.annotations.SerializedName

data class TriageRequest(
    @SerializedName("symptom") val symptom: String,
    @SerializedName("age_group") val ageGroup: String? = null,
    @SerializedName("duration") val duration: String? = null,
    @SerializedName("companions") val companions: List<String> = emptyList(),
    @SerializedName("elderly_mode") val elderlyMode: Boolean = false,
)

data class ReasoningStep(
    @SerializedName("step") val step: String,
    @SerializedName("internal") val internal: Boolean = false,
)

data class Source(
    @SerializedName("title") val title: String,
    @SerializedName("type") val type: String = "knowledge_base",
)

data class TriageResponse(
    @SerializedName("intent") val intent: String,
    @SerializedName("departments") val departments: List<String> = emptyList(),
    @SerializedName("guidance") val guidance: String = "",
    @SerializedName("confidence") val confidence: Double = 0.0,
    @SerializedName("sources") val sources: List<Source> = emptyList(),
    @SerializedName("red_flag") val redFlag: Boolean = false,
    @SerializedName("red_flag_hint") val redFlagHint: String? = null,
    @SerializedName("reasoning_steps") val reasoningSteps: List<ReasoningStep> = emptyList(),
    @SerializedName("disclaimer") val disclaimer: String = "",
    @SerializedName("elderly_mode") val elderlyMode: Boolean = false,
    @SerializedName("needs_clarification") val needsClarification: Boolean = false,
    @SerializedName("clarification_questions") val clarificationQuestions: List<String> = emptyList(),
)
