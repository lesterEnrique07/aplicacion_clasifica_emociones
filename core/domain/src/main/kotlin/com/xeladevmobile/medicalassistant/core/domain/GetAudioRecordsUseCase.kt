package com.xeladevmobile.medicalassistant.core.domain

import com.xeladevmobile.medicalassistant.core.data.repository.AudioRecordsRepository
import com.xeladevmobile.medicalassistant.core.data.repository.UserDataRepository
import javax.inject.Inject

class GetAudioRecordsUseCase @Inject constructor(
    private val audioRecordsRepository: AudioRecordsRepository,
    private val userDataRepository: UserDataRepository,
) {
    operator fun invoke() = audioRecordsRepository.getAudioRecords()
}