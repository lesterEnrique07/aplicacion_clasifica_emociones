package com.xeladevmobile.medicalassistant.core.data.repository

import android.media.AudioRecord
import com.xeladevmobile.medicalassistant.core.model.data.Audio
import kotlinx.coroutines.flow.Flow

interface AudioRecordsRepository {
    fun getAudioRecords(): Flow<List<Audio>>

    fun getAudio(audioId: String): Flow<Audio>

    suspend fun insertAudioRecord(patientId: String, filePath: String)
}