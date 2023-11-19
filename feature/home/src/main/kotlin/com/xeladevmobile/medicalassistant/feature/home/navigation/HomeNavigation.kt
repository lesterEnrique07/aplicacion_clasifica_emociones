package com.xeladevmobile.medicalassistant.feature.home.navigation

import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavOptions
import androidx.navigation.compose.composable
import androidx.navigation.navigation
import com.xeladevmobile.medicalassistant.feature.home.HomeScreenRoute

const val homeScreenRoute = "home_screen_route"
const val HOME_GRAPH_ROUTE_PATTERN = "home_graph"
private const val DEEP_LINK_URI_PATTERN =
    "https://www.medicalassistant.xeladevmobile.com/home"

fun NavController.navigateToHomeGraph(navOptions: NavOptions? = null) {
    this.navigate(HOME_GRAPH_ROUTE_PATTERN, navOptions)
}

fun NavGraphBuilder.homeGraph(onStartRecordingClick: () -> Unit, nestedGraphs: NavGraphBuilder.() -> Unit) {
    navigation(
        route = HOME_GRAPH_ROUTE_PATTERN,
        startDestination = homeScreenRoute,
    ) {
        composable(
            route = homeScreenRoute,
        ) {
            HomeScreenRoute(onStartRecordingClick)
        }

        nestedGraphs()
    }
}