package com.xeladevmobile.medicalassistant.core.data.repository

import com.xeladevmobile.core.database.dao.PatientDao
import com.xeladevmobile.core.database.model.PatientEntity
import com.xeladevmobile.core.network.MedicalNetworkDataSource
import com.xeladevmobile.medicalassistant.core.model.data.UserData
import javax.inject.Inject

class OfflineFirstPatientRepository @Inject constructor(
    private val patientDao: PatientDao,
    private val network: MedicalNetworkDataSource,
) : PatientRepository {
    override suspend fun insertPatient(userData: UserData) {
        val patientEntity = PatientEntity(
            id = userData.patientId,
            name = userData.name,
            surname = userData.name,
            address = userData.address,
            diagnosisDescription = userData.problemDescription,
            treatmentDate = userData.treatmentDate,
        )
        patientDao.insertPatient(patientEntity)
    }

    override suspend fun getPatientFromId(id: String): UserData {
        TODO("Not yet implemented")
    }

    override suspend fun getPatients(): List<UserData> {
        TODO("Not yet implemented")
    }

}