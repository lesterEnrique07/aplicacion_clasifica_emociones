package com.xeladevmobile.medicalassistant.feature.voice.navigation

import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavOptions
import androidx.navigation.compose.composable
import com.xeladevmobile.medicalassistant.feature.voice.VoiceScreenRoute

const val voiceRecordingRoute = "voice_recording_route"
private const val DEEP_LINK_URI_PATTERN =
    "https://www.medicalassistant.xeladevmobile.com/voice_recording"

fun NavController.navigateToRecordVoice(navOptions: NavOptions? = null) {
    this.navigate(voiceRecordingRoute, navOptions)
}

fun NavGraphBuilder.voiceRecordScreen(onRecordFinish: (String) -> Unit, onBackClick: () -> Unit) {
    composable(
        route = voiceRecordingRoute,
    ) {
        VoiceScreenRoute(onRecordFinish = onRecordFinish, onBackClick = onBackClick)
    }
}