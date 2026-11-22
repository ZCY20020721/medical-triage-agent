# 医导 · 安卓薄客户端

轻量化客户端：**只负责 UI + 调云端接口**，Agent 逻辑、知识图谱、LLM 全在云端后端。

## 运行步骤

1. 用 Android Studio 打开 `android/` 目录（需要 JDK 17 + Android SDK 35）。
2. 先启动云端后端（见 `../backend/README.md`），确保 `python run.py` 在 8000 端口运行。
3. 配置后端地址：
   - 模拟器：`RetrofitClient.BASE_URL` 保持 `http://10.0.2.2:8000/`（默认）。
   - 真机：改成电脑的局域网 IP，如 `http://192.168.1.100:8000/`。
4. 运行 `app` 到模拟器 / 真机。

## 功能

- 症状输入 → 云端导诊 → 展示「建议科室 + 就医指引 + 溯源 + 置信度 + 免责」。
- 高风险症状 → 红色紧急就医提示（不导诊）。
- 首次启动引导页：**标准模式 / 老人模式 二选一**，选择持久化（下次启动不再出现，首页仍可切换）。
- 老人模式：放大字号、去掉快捷标签/推理步骤/置信度等繁琐元素，只留一个病情输入框 + 一个大按钮。

## 目录结构

```
app/src/main/java/com/yidao/app/
├─ MainActivity.kt          # 入口
├─ ui/
│  ├─ AppRoot.kt            # 状态驱动页面切换 + Loading/Error
│  ├─ screens/
│  │  ├─ SymptomInputScreen.kt   # 输入页（含老人模式）
│  │  └─ TriageResultScreen.kt   # 结果页（含高风险兜底）
│  └─ theme/                # Material3 主题
├─ vm/TriageViewModel.kt    # 状态管理 + 调接口
└─ data/api/                # Retrofit 接口 + 数据模型 + 客户端
```
