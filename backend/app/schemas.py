"""API 请求/响应模型（对齐 Week 2 技术框架 4.1 导诊接口契约）。"""
from typing import List, Optional
from pydantic import BaseModel, Field


class TriageRequest(BaseModel):
    symptom: str = Field(..., min_length=1, description="症状描述，如「头痛发热三天」")
    age_group: Optional[str] = Field(None, description="成人/儿童/老人/孕妇")
    duration: Optional[str] = Field(None, description="持续时长")
    companions: List[str] = Field(default_factory=list, description="伴随症状")
    elderly_mode: bool = Field(False, description="老人模式：未指定人群时默认按老人处理")


class ReasoningStep(BaseModel):
    step: str
    internal: bool = Field(False, description="内部推导步骤，客户端不展示")


class Source(BaseModel):
    title: str
    type: str = "knowledge_base"


class TriageResponse(BaseModel):
    intent: str  # simple | complex | red_flag | tool | insufficient
    departments: List[str] = Field(default_factory=list)  # 仅科室名，绝不含疾病名
    guidance: str = ""
    confidence: float = 0.0
    sources: List[Source] = Field(default_factory=list)
    red_flag: bool = False
    red_flag_hint: Optional[str] = None
    reasoning_steps: List[ReasoningStep] = Field(default_factory=list)
    disclaimer: str = "本结果仅供参考，不构成诊断，身体不适应及时线下就医。"
    elderly_mode: bool = False
    needs_clarification: bool = False
    clarification_questions: List[str] = Field(default_factory=list)
