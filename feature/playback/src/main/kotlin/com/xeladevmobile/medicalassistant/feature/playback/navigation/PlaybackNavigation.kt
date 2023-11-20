package com.xeladevmobile.medicalassistant.feature.playback.navigation

import androidx.annotation.VisibleForTesting
import androidx.lifecycle.SavedStateHandle
import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavType
import androidx.navigation.compose.composable
import androidx.navigation.navArgument
import com.xeladevmobile.medicalassistant.feature.playback.PlaybackScreenRoute
import java.net.URLDecoder
import java.net.URLEncoder

private val URL_CHARACTER_ENCODING = Charsets.UTF_8.name()

@VisibleForTesting
internal const val audioIdArg = "audioId"

const val voicePlayRecordingRoute = "voice_play_recording_route"

internal class PlaybackArgs(val audioId: String) {
    constructor(savedStateHandle: SavedStateHandle) :
            this(URLDecoder.decode(checkNotNull(savedStateHandle[audioIdArg]), URL_CHARACTER_ENCODING))
}

fun NavController.navigateToPlayRecordVoice(audioId: String) {
    val encodedId = URLEncoder.encode(audioId, URL_CHARACTER_ENCODING)
    this.navigate("$voicePlayRecordingRoute/$encodedId") {
        launchSingleTop = true
    }
}

fun NavGraphBuilder.playVoiceRecordScreen(onBackClick: () -> Unit, onFinishClick: () -> Unit) {
    composable(
        route = "$voicePlayRecordingRoute/{$audioIdArg}",
        arguments = listOf(
            navArgument(audioIdArg) { type = NavType.StringType },
        ),
    ) {
        PlaybackScreenRoute(onBackClick = onBackClick, onFinishClick = onFinishClick)
    }
}