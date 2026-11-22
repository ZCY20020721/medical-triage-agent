package com.yidao.app.vm

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.yidao.app.data.Prefs
import com.yidao.app.data.api.RetrofitClient
import com.yidao.app.data.api.TriageRequest
import com.yidao.app.data.api.TriageResponse
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

sealed interface TriageUiState {
    data object Idle : TriageUiState
    data class Loading(val elderlyMode: Boolean) : TriageUiState
    data class Success(val response: TriageResponse, val elderlyMode: Boolean) : TriageUiState
    data class Error(val message: String, val elderlyMode: Boolean) : TriageUiState
}

class TriageViewModel(application: Application) : AndroidViewModel(application) {
    private val _state = MutableStateFlow<TriageUiState>(TriageUiState.Idle)
    val state: StateFlow<TriageUiState> = _state.asStateFlow()

    private val _elderlyMode = MutableStateFlow(Prefs.elderlyMode(application))
    val elderlyMode: StateFlow<Boolean> = _elderlyMode.asStateFlow()

    private val _onboarded = MutableStateFlow(Prefs.isOnboarded(application))
    val onboarded: StateFlow<Boolean> = _onboarded.asStateFlow()

    fun setElderlyMode(value: Boolean) {
        _elderlyMode.value = value
        Prefs.setMode(getApplication(), value)
    }

    fun completeOnboarding(elderly: Boolean) {
        setElderlyMode(elderly)
        _onboarded.value = true
    }

    fun submit(symptom: String) {
        if (symptom.isBlank()) return
        val elderly = _elderlyMode.value
        viewModelScope.launch {
            _state.value = TriageUiState.Loading(elderly)
            try {
                val resp = RetrofitClient.api.triage(
                    TriageRequest(symptom = symptom.trim(), elderlyMode = elderly)
                )
                _state.value = TriageUiState.Success(resp, elderly)
            } catch (e: Exception) {
                _state.value = TriageUiState.Error(e.message ?: "网络错误，请确认后端已启动", elderly)
            }
        }
    }

    fun reset() {
        _state.value = TriageUiState.Idle
    }
}
