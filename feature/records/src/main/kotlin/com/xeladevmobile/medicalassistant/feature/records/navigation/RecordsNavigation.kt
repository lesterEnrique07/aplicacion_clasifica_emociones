package com.xeladevmobile.medicalassistant.feature.records.navigation

import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavOptions
import androidx.navigation.compose.composable
import com.xeladevmobile.medicalassistant.core.model.data.Audio
import com.xeladevmobile.medicalassistant.feature.records.RecordsScreenRoute

const val recordsScreenRoute = "records_screen_route"
private const val DEEP_LINK_URI_PATTERN =
    "https://www.medicalassistant.xeladevmobile.com/records"

fun NavController.navigateToRecords(navOptions: NavOptions? = null) {
    this.navigate(recordsScreenRoute, navOptions)
}

fun NavGraphBuilder.recordsScreen(onElementClicked: (Audio) -> Unit) {
    composable(
        route = recordsScreenRoute,
    ) {
        RecordsScreenRoute(onElementClicked = onElementClicked)
    }
}