package com.xeladevmobile.core.database

import androidx.room.Database
import androidx.room.RoomDatabase
import com.xeladevmobile.core.database.dao.AudioDao
import com.xeladevmobile.core.database.dao.PatientDao
import com.xeladevmobile.core.database.model.AudioEntity
import com.xeladevmobile.core.database.model.PatientEntity

@Database(
    entities = [
        PatientEntity::class,
        AudioEntity::class,
    ],
    version = 1,
    exportSchema = true,
)
abstract class MedicalDatabase : RoomDatabase() {
    abstract fun patientDao(): PatientDao
    abstract fun audioDao(): AudioDao
}