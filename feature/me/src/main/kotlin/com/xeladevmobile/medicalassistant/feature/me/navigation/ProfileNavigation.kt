package com.xeladevmobile.medicalassistant.feature.me.navigation

import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavOptions
import androidx.navigation.compose.composable
import com.xeladevmobile.medicalassistant.feature.me.ProfileScreenRoute

const val profileScreenRoute = "profile_screen_route"
private const val DEEP_LINK_URI_PATTERN =
    "https://www.medicalassistant.xeladevmobile.com/me"

fun NavController.navigateToProfile(navOptions: NavOptions? = null) {
    this.navigate(profileScreenRoute, navOptions)
}

fun NavGraphBuilder.profileScreen() {
    composable(
        route = profileScreenRoute,
    ) {
        ProfileScreenRoute()
    }
}