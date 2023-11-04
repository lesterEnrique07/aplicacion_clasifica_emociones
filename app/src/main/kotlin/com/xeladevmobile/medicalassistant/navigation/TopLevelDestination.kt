package com.xeladevmobile.medicalassistant.navigation

import androidx.compose.ui.graphics.vector.ImageVector
import com.xeladevmobile.medicalassistant.core.designsystem.icon.MedicalIcons
import com.xeladevmobile.medicalassistant.feature.home.R as homeR
import com.xeladevmobile.medicalassistant.feature.me.R as meR
import com.xeladevmobile.medicalassistant.feature.records.R as recordsR

/**
 * Type for the top level destinations in the application. Each of these destinations
 * can contain one or more screens (based on the window size). Navigation from one screen to the
 * next within a single destination will be handled directly in composables.
 */
enum class TopLevelDestination(
    val selectedIcon: ImageVector,
    val unselectedIcon: ImageVector,
    val iconTextId: Int,
    val titleTextId: Int,
) {
    HOME(
        selectedIcon = MedicalIcons.Upcoming,
        unselectedIcon = MedicalIcons.UpcomingBorder,
        iconTextId = homeR.string.home,
        titleTextId = homeR.string.home,
    ),
    RECORDS(
        selectedIcon = MedicalIcons.Bookmarks,
        unselectedIcon = MedicalIcons.BookmarksBorder,
        iconTextId = recordsR.string.records,
        titleTextId = recordsR.string.records,
    ),
    ME(
        selectedIcon = MedicalIcons.Person,
        unselectedIcon = MedicalIcons.Person,
        iconTextId = meR.string.profile,
        titleTextId = meR.string.profile,
    ),
}
