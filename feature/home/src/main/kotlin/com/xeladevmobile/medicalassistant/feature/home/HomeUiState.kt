package com.xeladevmobile.medicalassistant.feature.home

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
    data object LoadFailed : HomeUiState

    /**
     * The home state has loaded.
     */
    data object Loaded : HomeUiState
}