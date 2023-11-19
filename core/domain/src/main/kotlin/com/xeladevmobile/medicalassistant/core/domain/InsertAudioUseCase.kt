package com.xeladevmobile.medicalassistant.core.domain

import com.xeladevmobile.medicalassistant.core.data.repository.AudioRecordsRepository
import com.xeladevmobile.medicalassistant.core.data.repository.UserDataRepository
import com.xeladevmobile.medicalassistant.core.model.data.Audio
import com.xeladevmobile.medicalassistant.core.model.data.Emotion
import javax.inject.Inject

class InsertAudioUseCase @Inject constructor(
    private val audioRecordsRepository: AudioRecordsRepository,
) {
    suspend operator fun invoke(filePath: String, patientId: String, emotion: Emotion) {
        audioRecordsRepository.insertAudioRecord(patientId, emotion, filePath)
    }
}