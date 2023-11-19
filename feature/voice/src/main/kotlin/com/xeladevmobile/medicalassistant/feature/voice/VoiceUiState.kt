package com.xeladevmobile.medicalassistant.feature.voice

sealed interface VoiceUiState {
    data object Recording : VoiceUiState
    data object Paused : VoiceUiState
    data object Stopped : VoiceUiState

    data class RecordSuccess(val audioId: String) : VoiceUiState
}