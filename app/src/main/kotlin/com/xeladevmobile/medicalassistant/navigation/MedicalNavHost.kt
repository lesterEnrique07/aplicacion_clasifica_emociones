package com.xeladevmobile.medicalassistant.navigation

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.NavOptions
import androidx.navigation.compose.NavHost
import com.xeladevmobile.medicalassistant.core.model.data.UserData
import com.xeladevmobile.medicalassistant.core.model.data.isLoggedIn
import com.xeladevmobile.medicalassistant.feature.home.navigation.HOME_GRAPH_ROUTE_PATTERN
import com.xeladevmobile.medicalassistant.feature.home.navigation.homeGraph
import com.xeladevmobile.medicalassistant.feature.home.navigation.navigateToHomeGraph
import com.xeladevmobile.medicalassistant.feature.login.navigation.LOGIN_GRAPH_ROUTE_PATTERN
import com.xeladevmobile.medicalassistant.feature.login.navigation.loginGraph
import com.xeladevmobile.medicalassistant.feature.me.navigation.profileScreen
import com.xeladevmobile.medicalassistant.feature.playback.navigation.navigateToPlayRecordVoice
import com.xeladevmobile.medicalassistant.feature.playback.navigation.playVoiceRecordScreen
import com.xeladevmobile.medicalassistant.feature.records.navigation.recordsScreen
import com.xeladevmobile.medicalassistant.feature.voice.navigation.navigateToRecordVoice
import com.xeladevmobile.medicalassistant.feature.voice.navigation.voiceRecordScreen
import com.xeladevmobile.medicalassistant.ui.MedicalAppState

/**
 * Top-level navigation graph. Navigation is organized as explained at
 * https://d.android.com/jetpack/compose/nav-adaptive
 *
 * The navigation graph defined in this file defines the different top level routes. Navigation
 * within each route is handled using state and Back Handlers.
 */
@Composable
fun MedicalNavHost(
    userData: UserData,
    appState: MedicalAppState,
    onShowSnackbar: suspend (String, String?) -> Boolean,
    modifier: Modifier = Modifier,
    startDestination: String =
        if (userData.isLoggedIn())
            HOME_GRAPH_ROUTE_PATTERN
        else
            LOGIN_GRAPH_ROUTE_PATTERN,
) {
    val navController = appState.navController
    NavHost(
        navController = navController,
        startDestination = startDestination,
        modifier = modifier,
    ) {
        loginGraph(
            onLoginSuccess = {
                navController.navigateToHomeGraph(
                    navOptions = NavOptions.Builder()
                        .setPopUpTo(LOGIN_GRAPH_ROUTE_PATTERN, true)
                        .build(),
                )
            },
            windowSizeClass = appState.windowSizeClass,
        )

        homeGraph(
            onStartRecordingClick = navController::navigateToRecordVoice,
            nestedGraphs = {
                voiceRecordScreen(
                    onRecordFinish = navController::navigateToPlayRecordVoice,
                    onBackClick = navController::popBackStack,
                )

                playVoiceRecordScreen(
                    onBackClick = {
                        navController.navigateToHomeGraph(
                            navOptions = NavOptions.Builder()
                                .setPopUpTo(HOME_GRAPH_ROUTE_PATTERN, true)
                                .build(),
                        )
                    },
                )
            },
        )
        recordsScreen(onElementClicked = { })
        profileScreen()
    }
}
