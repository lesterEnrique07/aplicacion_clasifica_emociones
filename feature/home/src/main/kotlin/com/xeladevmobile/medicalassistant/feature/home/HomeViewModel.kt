package com.xeladevmobile.medicalassistant.feature.home

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.xeladevmobile.medicalassistant.core.data.repository.UserDataRepository
import com.xeladevmobile.medicalassistant.core.domain.GetAudioRecordsUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.stateIn
import javax.inject.Inject

@HiltViewModel
class HomeViewModel @Inject constructor(
    userDataRepository: UserDataRepository,
    getAudioRecordsUseCase: GetAudioRecordsUseCase,
) : ViewModel() {
    val uiState: StateFlow<HomeUiState> = combine(
        userDataRepository.userData,
        getAudioRecordsUseCase(),
    ) { userData, audioRecords ->
        HomeUiState.Success(userData, audioRecords)
    }.stateIn(
        scope = viewModelScope,
        initialValue = HomeUiState.Loading,
        started = SharingStarted.WhileSubscribed(5_000),
    )
}