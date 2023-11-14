package com.xeladevmobile.core.database.model

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.Index
import androidx.room.PrimaryKey
import com.xeladevmobile.medicalassistant.core.model.data.Audio
import com.xeladevmobile.medicalassistant.core.model.data.Emotion

@Entity(
    tableName = "audio",
    foreignKeys = [
        ForeignKey(
            entity = PatientEntity::class,
            parentColumns = ["id"],
            childColumns = ["patient_id"],
            onDelete = ForeignKey.CASCADE,
        ),
    ],
    indices = [
        Index(value = ["patient_id"]),
    ],
)
data class AudioEntity(
    @PrimaryKey val id: String,
    val extension: String,
    val duration: Int,
    @ColumnInfo(name = "created_date")
    val createdDate: Long,
    val path: String,
    val emotion: Emotion,
    @ColumnInfo(name = "patient_id") val patientId: String,
)

fun AudioEntity.asExternalModel() = Audio(
    extension = extension,
    duration = duration,
    path = path,
    createdDate = createdDate,
    emotion = emotion,
)
