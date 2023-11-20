/*
 * Copyright 2023 Medical Assistant
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

package com.xeladevmobile.medicalassistant.core.testing.repository

import com.xeladevmobile.medicalassistant.core.data.repository.UserDataRepository
import com.xeladevmobile.medicalassistant.core.model.data.DarkThemeConfig
import com.xeladevmobile.medicalassistant.core.model.data.ThemeBrand
import com.xeladevmobile.medicalassistant.core.model.data.UserData
import com.xeladevmobile.medicalassistant.core.model.data.UserType
import kotlinx.coroutines.channels.BufferOverflow.DROP_OLDEST
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.filterNotNull

val emptyUserData = UserData(
    themeBrand = ThemeBrand.DEFAULT,
    darkThemeConfig = DarkThemeConfig.FOLLOW_SYSTEM,
    useDynamicColor = false,
    shouldHideOnboarding = false,
    address = "123 Main St",
    bornDate = "01/01/1990",
    experience = "10 years",
    graduationDate = "01/01/2010",
    name = "Jane Doe",
    occupation = "Doctor",
    sex = "Feminine",
    problemDescription = "",
    specialty = "Cardiologist",
    treatmentDate = "",
    userType = UserType.DOCTOR,
)

class TestUserDataRepository : UserDataRepository {
    /**
     * The backing hot flow for the list of followed topic ids for testing.
     */
    private val _userData = MutableSharedFlow<UserData>(replay = 1, onBufferOverflow = DROP_OLDEST)

    private val currentUserData get() = _userData.replayCache.firstOrNull() ?: emptyUserData

    override val userData: Flow<UserData> = _userData.filterNotNull()

    override suspend fun setThemeBrand(themeBrand: ThemeBrand) {
        currentUserData.let { current ->
            _userData.tryEmit(current.copy(themeBrand = themeBrand))
        }
    }

    override suspend fun setDarkThemeConfig(darkThemeConfig: DarkThemeConfig) {
        currentUserData.let { current ->
            _userData.tryEmit(current.copy(darkThemeConfig = darkThemeConfig))
        }
    }

    override suspend fun setDynamicColorPreference(useDynamicColor: Boolean) {
        currentUserData.let { current ->
            _userData.tryEmit(current.copy(useDynamicColor = useDynamicColor))
        }
    }

    override suspend fun setShouldHideOnboarding(shouldHideOnboarding: Boolean) {
        currentUserData.let { current ->
            _userData.tryEmit(current.copy(shouldHideOnboarding = shouldHideOnboarding))
        }
    }

    override suspend fun setFullName(fullName: String) {
        currentUserData.let { current ->
            _userData.tryEmit(current.copy(name = fullName))
        }
    }

    override suspend fun setSex(sex: String) {
        currentUserData.let { current ->
            _userData.tryEmit(current.copy(sex = sex))
        }
    }

    override suspend fun setBornDate(bornDate: String) {
        currentUserData.let { current ->
            _userData.tryEmit(current.copy(bornDate = bornDate))
        }
    }

    override suspend fun setAddress(address: String) {
        currentUserData.let { current ->
            _userData.tryEmit(current.copy(address = address))
        }
    }

    override suspend fun setUserType(userType: UserType) {
        currentUserData.let { current ->
            _userData.tryEmit(current.copy(userType = userType))
        }
    }

    override suspend fun setProblemDescription(problemDescription: String) {
        currentUserData.let { current ->
            _userData.tryEmit(current.copy(problemDescription = problemDescription))
        }
    }

    override suspend fun setTreatmentDate(treatmentDate: String) {
        currentUserData.let { current ->
            _userData.tryEmit(current.copy(treatmentDate = treatmentDate))
        }
    }

    override suspend fun setSpecialty(specialty: String) {
        currentUserData.let { current ->
            _userData.tryEmit(current.copy(specialty = specialty))
        }
    }

    override suspend fun setGraduationDate(graduationDate: String) {
        currentUserData.let { current ->
            _userData.tryEmit(current.copy(graduationDate = graduationDate))
        }
    }

    override suspend fun setExperience(experience: String) {
        currentUserData.let { current ->
            _userData.tryEmit(current.copy(experience = experience))
        }
    }

    override suspend fun setOccupation(occupation: String) {
        currentUserData.let { current ->
            _userData.tryEmit(current.copy(occupation = occupation))
        }
    }

    override suspend fun setUserFromNetwork(userData: UserData) {
        currentUserData.let { current ->
            _userData.tryEmit(
                current.copy(
                    name = userData.name,
                    address = userData.address,
                    bornDate = userData.bornDate,
                    experience = userData.experience,
                    graduationDate = userData.graduationDate,
                    occupation = userData.occupation,
                    sex = userData.sex,
                    specialty = userData.specialty,
                    treatmentDate = userData.treatmentDate,
                    userType = userData.userType,
                    problemDescription = userData.problemDescription,
                    shouldHideOnboarding = userData.shouldHideOnboarding,
                    useDynamicColor = userData.useDynamicColor,
                    themeBrand = userData.themeBrand,
                    darkThemeConfig = userData.darkThemeConfig,
                )
            )
        }
    }

    /**
     * A test-only API to allow setting of user data directly.
     */
    fun setUserData(userData: UserData) {
        _userData.tryEmit(userData)
    }
}
