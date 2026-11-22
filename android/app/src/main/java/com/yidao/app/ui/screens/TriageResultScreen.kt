package com.yidao.app.ui.screens

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.yidao.app.data.api.TriageResponse

@Composable
fun TriageResultScreen(response: TriageResponse, elderlyMode: Boolean, onBack: () -> Unit) {
    val bodySize = if (elderlyMode) 22.sp else 16.sp
    val deptSize = if (elderlyMode) 34.sp else 24.sp
    val titleSize = if (elderlyMode) 30.sp else 20.sp

    Scaffold { padding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
                .padding(if (elderlyMode) 20.dp else 16.dp)
                .verticalScroll(rememberScrollState()),
        ) {
            if (response.redFlag) {
                Text(
                    "请尽快线下就医",
                    fontSize = titleSize,
                    fontWeight = FontWeight.Bold,
                    color = MaterialTheme.colorScheme.error,
                )
                Spacer(Modifier.height(12.dp))
                Text(
                    response.redFlagHint ?: "该症状属高风险，请立即前往急诊或拨打 120。",
                    fontSize = deptSize,
                    color = MaterialTheme.colorScheme.error,
                )
            } else {
                Text("建议就诊科室", fontSize = bodySize)
                Spacer(Modifier.height(8.dp))
                if (response.departments.isEmpty()) {
                    Text(response.guidance, fontSize = deptSize, fontWeight = FontWeight.Bold)
                } else {
                    response.departments.forEach { d ->
                        Text(
                            d,
                            fontSize = deptSize,
                            fontWeight = FontWeight.Bold,
                            color = MaterialTheme.colorScheme.primary,
                        )
                    }
                }

                Spacer(Modifier.height(16.dp))
                Text("就医指引", fontSize = bodySize, fontWeight = FontWeight.Bold)
                Spacer(Modifier.height(4.dp))
                Text(response.guidance, fontSize = bodySize)

                if (!elderlyMode) {
                    if (response.reasoningSteps.isNotEmpty()) {
                        Spacer(Modifier.height(16.dp))
                        Text("推理过程", fontSize = 16.sp, fontWeight = FontWeight.Bold)
                        response.reasoningSteps.filterNot { it.internal }.forEach { step ->
                            Text("· ${step.step}", fontSize = 14.sp)
                        }
                    }
                    Spacer(Modifier.height(12.dp))
                    Text("置信度：${(response.confidence * 100).toInt()}%", fontSize = 14.sp)
                    if (response.sources.isNotEmpty()) {
                        Spacer(Modifier.height(4.dp))
                        Text(
                            "来源：${response.sources.joinToString("；") { it.title }}",
                            fontSize = 12.sp,
                            color = MaterialTheme.colorScheme.onSurfaceVariant,
                        )
                    }
                }
            }

            Spacer(Modifier.height(20.dp))
            Text(
                response.disclaimer,
                fontSize = if (elderlyMode) 18.sp else 12.sp,
                color = MaterialTheme.colorScheme.onSurfaceVariant,
            )

            Spacer(Modifier.height(24.dp))
            Button(
                onClick = onBack,
                modifier = Modifier.fillMaxWidth().height(if (elderlyMode) 64.dp else 52.dp),
            ) {
                Text("返回", fontSize = if (elderlyMode) 24.sp else 16.sp)
            }
        }
    }
}
