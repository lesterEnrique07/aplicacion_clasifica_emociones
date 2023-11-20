package com.xeladevmobile.medicalassistant.feature.login.navigation

import androidx.compose.material3.windowsizeclass.WindowSizeClass
import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import com.xeladevmobile.medicalassistant.feature.login.LoginRoute

const val LOGIN_GRAPH_ROUTE_PATTERN = "login_graph"

fun NavGraphBuilder.loginGraph(onLoginSuccess: () -> Unit, windowSizeClass: WindowSizeClass) {
    composable(
        route = LOGIN_GRAPH_ROUTE_PATTERN,
    ) {
        LoginRoute(onLoginSuccess = onLoginSuccess, windowSizeClass = windowSizeClass)
    }
}