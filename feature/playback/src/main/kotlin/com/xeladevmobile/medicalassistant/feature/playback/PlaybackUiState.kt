package com.xeladevmobile.medicalassistant.feature.playback

sealed interface PlaybackUiState {
    data object Stopped : PlaybackUiState
    data object Playing : PlaybackUiState
    data object Paused : PlaybackUiState

    data object Ready : PlaybackUiState
}

val PlaybackUiState.isPlaying: Boolean
    get() {
        return this is PlaybackUiState.Playing
    }

val PlaybackUiState.isPaused: Boolean
    get() {
        return this is PlaybackUiState.Paused
    }