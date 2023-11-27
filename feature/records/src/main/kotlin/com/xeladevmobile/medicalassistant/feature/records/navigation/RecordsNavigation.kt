package com.xeladevmobile.medicalassistant.feature.records.navigation

import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavOptions
import androidx.navigation.compose.composable
import androidx.navigation.navigation
import com.xeladevmobile.medicalassistant.feature.records.RecordsScreenRoute

const val RECORDS_GRAPH_PATTERN = "records_graph"
const val recordsScreenRoute = "records_screen_route"
private const val DEEP_LINK_URI_PATTERN =
    "https://www.medicalassistant.xeladevmobile.com/records"

fun NavController.navigateToRecordsGraph(navOptions: NavOptions? = null) {
    this.navigate(RECORDS_GRAPH_PATTERN, navOptions)
}

fun NavGraphBuilder.recordsGraph(onElementClicked: (String) -> Unit, nestedGraphs: NavGraphBuilder.() -> Unit) {
    navigation(
        route = RECORDS_GRAPH_PATTERN,
        startDestination = recordsScreenRoute,
    ) {
        composable(
            route = recordsScreenRoute,
        ) {
            RecordsScreenRoute(onElementClicked = onElementClicked)
        }

        nestedGraphs()
    }
}