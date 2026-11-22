package com.yidao.app.ui.screens

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Card
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

/** 首次启动引导页：标准模式 / 老人模式 二选一。 */
@Composable
fun OnboardingScreen(onSelect: (Boolean) -> Unit) {
    Scaffold { padding ->
        Column(
            modifier = Modifier.fillMaxSize().padding(padding).padding(24.dp),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally,
        ) {
            Text("医导", fontSize = 42.sp, fontWeight = FontWeight.Bold)
            Spacer(Modifier.height(8.dp))
            Text("帮你判断该挂哪个科，不做诊断", fontSize = 18.sp)
            Spacer(Modifier.height(44.dp))
            Text("请选择使用模式", fontSize = 20.sp, fontWeight = FontWeight.Bold)
            Spacer(Modifier.height(24.dp))

            Card(
                modifier = Modifier.fillMaxWidth().clickable { onSelect(false) },
            ) {
                Column(Modifier.padding(22.dp)) {
                    Text(
                        "标准模式",
                        fontSize = 24.sp,
                        fontWeight = FontWeight.Bold,
                        color = MaterialTheme.colorScheme.primary,
                    )
                    Spacer(Modifier.height(8.dp))
                    Text("完整功能：快捷标签、推理过程、溯源、置信度", fontSize = 15.sp)
                }
            }
            Spacer(Modifier.height(20.dp))

            Card(
                modifier = Modifier.fillMaxWidth().clickable { onSelect(true) },
            ) {
                Column(Modifier.padding(22.dp)) {
                    Text(
                        "老人模式",
                        fontSize = 24.sp,
                        fontWeight = FontWeight.Bold,
                        color = MaterialTheme.colorScheme.primary,
                    )
                    Spacer(Modifier.height(8.dp))
                    Text("大字、极简：只输入病情，一键看病", fontSize = 15.sp)
                }
            }
        }
    }
}
