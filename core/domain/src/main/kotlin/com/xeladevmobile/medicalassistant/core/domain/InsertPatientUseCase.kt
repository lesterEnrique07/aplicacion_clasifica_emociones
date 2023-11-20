package com.xeladevmobile.medicalassistant.core.domain

import com.xeladevmobile.medicalassistant.core.data.repository.PatientRepository
import com.xeladevmobile.medicalassistant.core.model.data.UserData
import javax.inject.Inject

class InsertPatientUseCase @Inject constructor(
    private val patientRepository: PatientRepository,
) {
    suspend operator fun invoke(userData: UserData) = patientRepository.insertPatient(userData)
}