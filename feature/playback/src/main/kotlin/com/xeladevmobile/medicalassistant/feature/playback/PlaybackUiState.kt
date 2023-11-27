package com.xeladevmobile.medicalassistant.feature.playback

import com.xeladevmobile.medicalassistant.core.model.data.Audio
import com.xeladevmobile.medicalassistant.core.model.data.Emotion

sealed interface PlaybackUiState {
    data class Analyzed(val result: Emotion) : PlaybackUiState
    data object Stopped : PlaybackUiState
    data object Playing : PlaybackUiState
    data object Paused : PlaybackUiState
    data object Loading : PlaybackUiState

    data object Error : PlaybackUiState
}

val PlaybackUiState.isPlaying: Boolean
    get() {
        return this is PlaybackUiState.Playing
    }

val PlaybackUiState.isPaused: Boolean
    get() {
        return this is PlaybackUiState.Paused
    }