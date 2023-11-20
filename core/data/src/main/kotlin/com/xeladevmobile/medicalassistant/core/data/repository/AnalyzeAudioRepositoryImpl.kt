package com.xeladevmobile.medicalassistant.core.data.repository

import com.xeladevmobile.core.network.MedicalNetworkDataSource
import com.xeladevmobile.medicalassistant.core.model.data.Emotion
import java.io.File
import javax.inject.Inject

class AnalyzeAudioRepositoryImpl @Inject constructor(
    private val networkDataSource: MedicalNetworkDataSource,
) : AnalyzeAudioRepository {
    override suspend fun analyzeAudio(filePath: String): Emotion {
        val result = networkDataSource.predictEmotion(File(filePath))
        return Emotion.valueOf(result.prediction.replace("\"", ""))
    }
}