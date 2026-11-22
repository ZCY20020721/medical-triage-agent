package com.yidao.app.ui.screens

import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.AssistChip
import androidx.compose.material3.Button
import androidx.compose.material3.LocalTextStyle
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Switch
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

private val QUICK_SYMPTOMS = listOf("头痛", "发热", "咳嗽", "腹痛", "牙痛", "胸痛")

@Composable
fun SymptomInputScreen(
    elderlyMode: Boolean,
    onElderlyModeChange: (Boolean) -> Unit,
    onSubmit: (String) -> Unit,
) {
    var text by rememberSaveable { mutableStateOf("") }
    val titleSize = if (elderlyMode) 30.sp else 20.sp
    val bodySize = if (elderlyMode) 22.sp else 16.sp
    val inputSize = if (elderlyMode) 24.sp else 16.sp

    Scaffold { padding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
                .padding(horizontal = if (elderlyMode) 20.dp else 16.dp)
                .verticalScroll(rememberScrollState()),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally,
        ) {
            Text("医导", fontSize = if (elderlyMode) 42.sp else 28.sp, fontWeight = FontWeight.Bold)
            Text("帮你判断该挂哪个科，不做诊断", fontSize = bodySize)
            Spacer(Modifier.height(20.dp))

            Row(verticalAlignment = Alignment.CenterVertically) {
                Text("老人模式", fontSize = bodySize)
                Spacer(Modifier.width(12.dp))
                Switch(checked = elderlyMode, onCheckedChange = onElderlyModeChange)
            }
            Spacer(Modifier.height(20.dp))

            OutlinedTextField(
                value = text,
                onValueChange = { text = it },
                modifier = Modifier.fillMaxWidth(),
                minLines = if (elderlyMode) 3 else 2,
                textStyle = LocalTextStyle.current.copy(fontSize = inputSize),
                placeholder = {
                    Text(
                        if (elderlyMode) "哪里不舒服，写在这里" else "描述你的症状，如：头痛发热三天",
                        fontSize = inputSize,
                    )
                },
                label = { Text("病情描述", fontSize = bodySize) },
            )

            if (!elderlyMode) {
                Spacer(Modifier.height(12.dp))
                Row(
                    modifier = Modifier.fillMaxWidth().horizontalScroll(rememberScrollState()),
                    horizontalArrangement = Arrangement.spacedBy(8.dp),
                ) {
                    QUICK_SYMPTOMS.forEach { s ->
                        AssistChip(onClick = { text = s }, label = { Text(s) })
                    }
                }
            }

            Spacer(Modifier.height(24.dp))

            Button(
                onClick = { onSubmit(text) },
                modifier = Modifier
                    .fillMaxWidth()
                    .height(if (elderlyMode) 64.dp else 52.dp),
            ) {
                Text("我要看病", fontSize = if (elderlyMode) 26.sp else 18.sp)
            }

            Spacer(Modifier.height(16.dp))
            Text(
                "本结果仅供参考，不构成诊断，身体不适应及时线下就医。",
                fontSize = if (elderlyMode) 18.sp else 12.sp,
                color = MaterialTheme.colorScheme.onSurfaceVariant,
            )
        }
    }
}
