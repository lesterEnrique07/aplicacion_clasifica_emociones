package com.xeladevmobile.core.database.dao

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import com.xeladevmobile.core.database.model.AudioEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface AudioDao {

    // Insert an audio record
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAudio(audio: AudioEntity)

    // Update an audio record
    @Update
    suspend fun updateAudio(audio: AudioEntity)

    // Delete an audio record
    @Delete
    suspend fun deleteAudio(audio: AudioEntity)

    // Query to retrieve a single audio by its id
    @Query("SELECT * FROM audio WHERE id = :audioId")
    fun getAudioById(audioId: String): Flow<AudioEntity>

    // Query to retrieve all audios
    @Query("SELECT * FROM audio")
    fun getAllAudios(): Flow<List<AudioEntity>>
}
