package com.xeladevmobile.medicalassistant.feature.records

import com.xeladevmobile.medicalassistant.core.model.data.UserData

/**
 * A sealed hierarchy describing the state of the records.
 */
sealed interface RecordsUiState {
    /**
     * The feed is still loading.
     */
    data object Loading : RecordsUiState

    /**
     * The feed is empty.
     */
    data object Empty : RecordsUiState

    /**
     * The feed is loaded with the given data. In the case of patient profile
     * will reflect their records. In the case of the doctor profile will reflect
     * their patients and their records.
     */
    data class Success(
        val feed: List<UserData>,
    ) : RecordsUiState
}