package com.xeladevmobile.feature.me

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.xeladevmobile.medicalassistant.core.data.repository.UserDataRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import javax.inject.Inject

@HiltViewModel
class ProfileViewModel @Inject constructor(
    userDataRepository: UserDataRepository,
) : ViewModel() {
    val uiState: StateFlow<ProfileUiState> = userDataRepository.userData.map {
        ProfileUiState.Success(it)
    }.stateIn(
        scope = viewModelScope,
        initialValue = ProfileUiState.Loading,
        started = SharingStarted.WhileSubscribed(5_000),
    )
}