package com.yidao.app.ui

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.yidao.app.ui.screens.OnboardingScreen
import com.yidao.app.ui.screens.SymptomInputScreen
import com.yidao.app.ui.screens.TriageResultScreen
import com.yidao.app.vm.TriageUiState
import com.yidao.app.vm.TriageViewModel

@Composable
fun AppRoot(viewModel: TriageViewModel = viewModel()) {
    val onboarded by viewModel.onboarded.collectAsState()

    // 首次启动：先走「标准模式 / 老人模式」二选一引导页
    if (!onboarded) {
        OnboardingScreen(onSelect = viewModel::completeOnboarding)
        return
    }

    val state by viewModel.state.collectAsState()
    val elderlyMode by viewModel.elderlyMode.collectAsState()

    when (val s = state) {
        is TriageUiState.Idle -> SymptomInputScreen(
            elderlyMode = elderlyMode,
            onElderlyModeChange = viewModel::setElderlyMode,
            onSubmit = viewModel::submit,
        )
        is TriageUiState.Loading -> LoadingScreen(s.elderlyMode)
        is TriageUiState.Success -> TriageResultScreen(s.response, s.elderlyMode, viewModel::reset)
        is TriageUiState.Error -> ErrorScreen(s.message, s.elderlyMode, viewModel::reset)
    }
}

@Composable
private fun LoadingScreen(elderlyMode: Boolean) {
    Scaffold { padding ->
        Column(
            modifier = Modifier.fillMaxSize().padding(padding),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally,
        ) {
            CircularProgressIndicator()
            Text("正在分析…", fontSize = if (elderlyMode) 22.sp else 16.sp)
        }
    }
}

@Composable
private fun ErrorScreen(message: String, elderlyMode: Boolean, onBack: () -> Unit) {
    Scaffold { padding ->
        Column(
            modifier = Modifier.fillMaxSize().padding(padding).padding(20.dp),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally,
        ) {
            Text("出错了", fontSize = if (elderlyMode) 28.sp else 20.sp, color = MaterialTheme.colorScheme.error)
            Text(message, fontSize = if (elderlyMode) 22.sp else 16.sp)
            Button(onClick = onBack) { Text("返回", fontSize = if (elderlyMode) 24.sp else 16.sp) }
        }
    }
}
