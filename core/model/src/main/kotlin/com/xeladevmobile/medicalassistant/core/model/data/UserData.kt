/*
 * Copyright 2022 Medical Assistant
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     https://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.xeladevmobile.medicalassistant.core.model.data

import java.util.UUID

/**
 * Class summarizing user interest data
 */
data class UserData(
    val themeBrand: ThemeBrand,
    val darkThemeConfig: DarkThemeConfig,
    val useDynamicColor: Boolean,
    val shouldHideOnboarding: Boolean,
    val name: String,
    val sex: String,
    val bornDate: String,
    val address: String,
    val userType: UserType,

    // Patient
    val patientId: String,
    val problemDescription: String,
    val treatmentDate: String,

    // Doctor
    val doctorId: String,
    val specialty: String,
    val graduationDate: String,
    val experience: String,
    val occupation: String,
)

fun UserData.isLoggedIn(): Boolean {
    // Assuming that a non-empty name indicates a logged-in user.
    // Add additional checks as necessary.
    return name.isNotEmpty()
}

enum class UserType {
    DOCTOR, PATIENT
}

val patientUserData = UserData(
    themeBrand = ThemeBrand.ANDROID,
    darkThemeConfig = DarkThemeConfig.DARK,
    useDynamicColor = true,
    shouldHideOnboarding = true,
    name = "Alice Johnson",
    sex = "Female",
    bornDate = "1985-04-23",
    address = "123 Apple Street, New York, NY",
    userType = UserType.PATIENT,
    problemDescription = "Seasonal allergies",
    treatmentDate = "2023-04-10",
    specialty = "",
    graduationDate = "",
    experience = "",
    occupation = "",
    patientId = UUID.randomUUID().toString(),
    doctorId = "",
)

// Mock doctor data
val doctorUserData = UserData(
    themeBrand = ThemeBrand.DEFAULT,
    darkThemeConfig = DarkThemeConfig.FOLLOW_SYSTEM,
    useDynamicColor = false,
    shouldHideOnboarding = false,
    name = "Dr. John Smith",
    sex = "Male",
    bornDate = "1975-08-15",
    address = "456 Orange Avenue, Los Angeles, CA",
    userType = UserType.DOCTOR,
    specialty = "Pediatrics",
    graduationDate = "2000-06-20",
    experience = "20",
    occupation = "Pediatrician",
    problemDescription = "",
    treatmentDate = "",
    patientId = "",
    doctorId = UUID.randomUUID().toString(),
)