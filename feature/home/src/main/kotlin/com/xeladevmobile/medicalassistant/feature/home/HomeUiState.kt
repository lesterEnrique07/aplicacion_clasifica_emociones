package com.xeladevmobile.medicalassistant.feature.home

import com.xeladevmobile.medicalassistant.core.model.data.Audio
import com.xeladevmobile.medicalassistant.core.model.data.UserData

/**
 * A sealed hierarchy describing the onboarding state for the for you screen.
 */
sealed interface HomeUiState {
    /**
     * The home state is loading.
     */
    data object Loading : HomeUiState

    /**
     * The home state was unable to load.
     */
    data class Success(val userData: UserData, val audioRecords: List<Audio>) : HomeUiState
}