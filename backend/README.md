# 医导 · 云端导诊 Agent 后端

轻量化架构：**安卓薄客户端 → 本服务（FastAPI）→ 云 LLM API（DeepSeek / GLM）**。
Agent 全部逻辑、知识图谱、合规红线都在云端，客户端只负责 UI 与调接口。

## 快速开始

```bash
cd backend
python -m pip install -r requirements.txt

# （可选）配置 LLM：复制 .env.example 为 .env 并填入 Key
# 不配置也能跑——自动降级到内置词典 + 模板规则引擎

python run.py        # 启动于 http://0.0.0.0:8000
```

## 接口

| 方法 | 路径 | 说明 |
|---|---|---|
| GET | `/health` | 健康检查，返回 LLM 是否启用 |
| POST | `/api/v1/triage` | 导诊问答（对齐 Week 2 技术框架 4.1 契约） |

### 请求示例

```json
{
  "symptom": "头痛发热三天，家里老人",
  "age_group": null,
  "duration": "3天",
  "companions": [],
  "elderly_mode": false
}
```

### 响应示例

```json
{
  "intent": "complex",
  "departments": ["神经内科"],
  "guidance": "建议前往神经内科就诊……",
  "confidence": 0.7,
  "sources": [{"title": "导诊知识库 · 头痛/发热", "type": "knowledge_base"}],
  "red_flag": false,
  "red_flag_hint": null,
  "reasoning_steps": [
    {"step": "识别症状：头痛、发热", "internal": false},
    {"step": "识别人群：老人", "internal": false},
    {"step": "确定建议科室：神经内科", "internal": false}
  ],
  "disclaimer": "本结果仅供参考，不构成诊断，身体不适应及时线下就医。",
  "elderly_mode": false
}
```

## 目录结构

```
app/
├─ main.py          # FastAPI 入口 + 路由
├─ pipeline.py      # 导诊流水线编排
├─ schemas.py       # 请求/响应模型
├─ config.py        # 环境配置
├─ agent/           # Agent 核心：router / extractor / planner / guard
├─ kg/              # 迷你知识图谱：data（种子）+ graph（查询）
└─ llm/             # OpenAI 兼容 LLM 客户端（DeepSeek/GLM）
```
