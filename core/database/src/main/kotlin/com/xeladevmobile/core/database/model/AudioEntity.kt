package com.xeladevmobile.core.database.model

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.Index
import androidx.room.PrimaryKey

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
    @ColumnInfo(name = "patient_id") val patientId: String,
)
