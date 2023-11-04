package com.xeladevmobile.core.database

import com.xeladevmobile.core.database.dao.AudioDao
import com.xeladevmobile.core.database.dao.PatientDao
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent

@Module
@InstallIn(SingletonComponent::class)
object DaosModule {
    @Provides
    fun providesPatientsDao(
        database: MedicalDatabase,
    ): PatientDao = database.patientDao()

    @Provides
    fun providesAudioDao(
        database: MedicalDatabase,
    ): AudioDao = database.audioDao()
}