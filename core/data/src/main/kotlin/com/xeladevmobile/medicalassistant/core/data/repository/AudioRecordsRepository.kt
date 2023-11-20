package com.xeladevmobile.medicalassistant.core.data.repository

import com.xeladevmobile.medicalassistant.core.model.data.Audio
import com.xeladevmobile.medicalassistant.core.model.data.Emotion
import kotlinx.coroutines.flow.Flow

interface AudioRecordsRepository {
    fun getAudioRecords(): Flow<List<Audio>>

    fun getAudio(audioId: String): Flow<Audio?>

    suspend fun insertAudioRecord(patientId: String, emotion: Emotion, filePath: String)
}