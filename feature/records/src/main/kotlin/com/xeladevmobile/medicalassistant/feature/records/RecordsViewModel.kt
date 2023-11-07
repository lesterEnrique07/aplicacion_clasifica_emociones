package com.xeladevmobile.medicalassistant.feature.records

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.xeladevmobile.medicalassistant.core.data.repository.UserDataRepository
import com.xeladevmobile.medicalassistant.core.domain.GetAudioRecordsUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import javax.inject.Inject

@HiltViewModel
class RecordsViewModel @Inject constructor(
    private val userDataRepository: UserDataRepository,
    private val getAudioRecordsUseCase: GetAudioRecordsUseCase,
) : ViewModel() {
    val uiState: StateFlow<RecordsUiState> = getAudioRecordsUseCase().map {
        if (it.isEmpty()) {
            RecordsUiState.Empty
        } else {
            RecordsUiState.Success(it)
        }
    }.stateIn(
        scope = viewModelScope,
        initialValue = RecordsUiState.Loading,
        started = SharingStarted.WhileSubscribed(5_000),
    )
}