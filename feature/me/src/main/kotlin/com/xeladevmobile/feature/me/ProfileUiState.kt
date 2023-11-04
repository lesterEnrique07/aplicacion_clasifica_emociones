package com.xeladevmobile.feature.me

import com.xeladevmobile.medicalassistant.core.model.data.UserData

/**
 * A sealed hierarchy describing the state of the profile.
 */
sealed interface ProfileUiState {
    /**
     * The feed is still loading.
     */
    data object Loading : ProfileUiState

    /**
     * The profile is loaded with the given user data.
     */
    data class Success(
        val user: UserData,
    ) : ProfileUiState
}