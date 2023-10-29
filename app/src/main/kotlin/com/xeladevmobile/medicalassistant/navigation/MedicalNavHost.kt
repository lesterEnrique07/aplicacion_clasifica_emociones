package com.xeladevmobile.medicalassistant.navigation

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.compose.NavHost
import com.xeladevmobile.feature.me.navigation.profileScreen
import com.xeladevmobile.medicalassistant.feature.home.navigation.homeScreen
import com.xeladevmobile.medicalassistant.feature.home.navigation.homeScreenRoute
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
    appState: MedicalAppState,
    onShowSnackbar: suspend (String, String?) -> Boolean,
    modifier: Modifier = Modifier,
    startDestination: String = homeScreenRoute,
) {
    val navController = appState.navController
    NavHost(
        navController = navController,
        startDestination = startDestination,
        modifier = modifier,
    ) {
        homeScreen(onDashboardClick = { })
        profileScreen()
    }
}
