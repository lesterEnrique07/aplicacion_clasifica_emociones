package com.xeladevmobile.medicalassistant.core.data.repository

import com.xeladevmobile.medicalassistant.core.model.data.UserData

interface PatientRepository {
    suspend fun insertPatient(userData: UserData)

    suspend fun getPatientFromId(id: String): UserData

    suspend fun getPatients(): List<UserData>
}