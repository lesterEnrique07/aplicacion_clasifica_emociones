package com.xeladevmobile.core.database.model

import androidx.room.ColumnInfo
import androidx.room.Embedded
import androidx.room.Entity
import androidx.room.PrimaryKey
import androidx.room.Relation

@Entity(
    tableName = "patients",
)
data class PatientEntity(
    @PrimaryKey val id: String,
    val name: String,
    val surname: String,
    @ColumnInfo(name = "diagnosis_description")
    val diagnosisDescription: String,
    @ColumnInfo(name = "treatment_date")
    val treatmentDate: Long,
    val address: String,
)

data class PatientWithAudios(
    @Embedded val patient: PatientEntity,
    @Relation(
        parentColumn = "id",
        entityColumn = "patient_id",
    )
    val audios: List<AudioEntity>,
)