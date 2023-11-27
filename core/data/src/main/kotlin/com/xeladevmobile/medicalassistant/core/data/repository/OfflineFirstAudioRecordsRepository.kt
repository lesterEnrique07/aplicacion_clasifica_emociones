package com.xeladevmobile.medicalassistant.core.data.repository

import android.media.MediaMetadataRetriever
import com.xeladevmobile.core.database.dao.AudioDao
import com.xeladevmobile.core.database.model.AudioEntity
import com.xeladevmobile.core.database.model.asExternalModel
import com.xeladevmobile.medicalassistant.core.model.data.Audio
import com.xeladevmobile.medicalassistant.core.model.data.Emotion
import com.xeladevmobile.medicalassistant.core.network.MedicalNetworkDataSource
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.withContext
import javax.inject.Inject

class OfflineFirstAudioRecordsRepository @Inject constructor(
    private val audioDao: AudioDao,
    private val network: MedicalNetworkDataSource,
) : AudioRecordsRepository {
    override fun getAudioRecords(): Flow<List<Audio>> =
        audioDao.getAllAudios().map { it.map(AudioEntity::asExternalModel) }

    override fun getAudio(audioId: String): Flow<Audio?> {
        return audioDao.getAudioById(audioId).map { it?.asExternalModel() }
    }

    override suspend fun insertAudioRecord(patientId: String, emotion: Emotion, filePath: String) =
        withContext(Dispatchers.IO) {
            val retriever = MediaMetadataRetriever()
            try {
                retriever.setDataSource(filePath)

                val duration = retriever.extractMetadata(MediaMetadataRetriever.METADATA_KEY_DURATION)?.toInt() ?: 0
                val mimeType = retriever.extractMetadata(MediaMetadataRetriever.METADATA_KEY_MIMETYPE)

                // Assuming getFileExtension is a function you implement to extract the file extension from the mimeType or filePath
                val extension = getFileExtension(mimeType, filePath)

                val audioEntity = AudioEntity(
                    id = filePath.substringAfterLast("_").substringBeforeLast("."),
                    patientId = patientId,
                    extension = extension,
                    duration = duration,
                    path = filePath,
                    createdDate = System.currentTimeMillis(),
                    emotion = emotion,
                )

                // Insert the audio entity into the database
                audioDao.insertAudio(audioEntity)
            } catch (e: Exception) {
                e.printStackTrace()
            } finally {
                retriever.release() // Make sure to release the retriever
            }
        }

    private fun getFileExtension(mimeType: String?, filePath: String): String {
        // Implement logic to derive file extension based on mimeType or file path.
        // For instance, if the mimeType is "audio/mp4", the extension could be "mp4".
        // If mimeType is not reliable or not available, parse the file extension from filePath.
        // This is a placeholder implementation.
        return mimeType?.substringAfterLast("/") ?: filePath.substringAfterLast(".")
    }
}