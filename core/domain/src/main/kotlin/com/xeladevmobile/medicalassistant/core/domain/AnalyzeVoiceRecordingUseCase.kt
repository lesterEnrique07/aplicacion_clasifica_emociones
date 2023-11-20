package com.xeladevmobile.medicalassistant.core.domain

import com.xeladevmobile.medicalassistant.core.data.repository.AnalyzeAudioRepository
import javax.inject.Inject

class AnalyzeVoiceRecordingUseCase @Inject constructor(
    private val voiceRepository: AnalyzeAudioRepository,
) {
    suspend operator fun invoke(filePath: String) {
        voiceRepository.analyzeAudio(filePath)
    }
}