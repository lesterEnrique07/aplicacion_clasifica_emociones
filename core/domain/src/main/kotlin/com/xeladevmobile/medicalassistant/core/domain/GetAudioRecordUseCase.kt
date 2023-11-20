package com.xeladevmobile.medicalassistant.core.domain

import com.xeladevmobile.medicalassistant.core.data.repository.AudioRecordsRepository
import javax.inject.Inject

class GetAudioRecordUseCase @Inject constructor(
    private val audioRecordsRepository: AudioRecordsRepository,
) {
    operator fun invoke(audioId: String) = audioRecordsRepository.getAudio(audioId)
}