package com.xeladevmobile.medicalassistant.core.data.repository

import com.xeladevmobile.medicalassistant.core.model.data.Emotion

interface AnalyzeAudioRepository {
    suspend fun analyzeAudio(filePath: String): Emotion
}