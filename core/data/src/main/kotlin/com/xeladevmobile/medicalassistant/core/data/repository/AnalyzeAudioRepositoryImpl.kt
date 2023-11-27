package com.xeladevmobile.medicalassistant.core.data.repository

import com.xeladevmobile.medicalassistant.core.common.result.Result
import com.xeladevmobile.medicalassistant.core.model.data.Emotion
import com.xeladevmobile.medicalassistant.core.network.MedicalNetworkDataSource
import java.io.File
import javax.inject.Inject

class AnalyzeAudioRepositoryImpl @Inject constructor(
    private val networkDataSource: MedicalNetworkDataSource,
) : AnalyzeAudioRepository {
    override suspend fun analyzeAudio(filePath: String): Emotion? {
        return try {
            when (val result = networkDataSource.predictEmotion(File(filePath))) {
                is Result.Success -> {
                    Emotion.valueOf(result.data.prediction.replace("\"", ""))
                }

                is Result.Error, Result.Loading -> {
                    null
                }
            }
        } catch (_: Exception) {
            null
        }
    }
}