package com.xeladevmobile.medicalassistant.feature.home.navigation

import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavOptions
import androidx.navigation.compose.composable
import com.xeladevmobile.medicalassistant.feature.home.HomeScreenRoute

const val homeScreenRoute = "home_screen_route"
private const val DEEP_LINK_URI_PATTERN =
    "https://www.medicalassistant.xeladevmobile.com/home"

fun NavController.navigateToHome(navOptions: NavOptions? = null) {
    this.navigate(DEEP_LINK_URI_PATTERN, navOptions)
}

fun NavGraphBuilder.homeScreen(onDashboardClick: (String) -> Unit) {
    composable(
        route = DEEP_LINK_URI_PATTERN,
    ) {
        HomeScreenRoute(onDashboardClick)
    }
}