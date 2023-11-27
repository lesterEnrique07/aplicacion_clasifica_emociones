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

package com.xeladevmobile.medicalassistant.core.data.repository

import com.xeladevmobile.medicalassistant.core.model.data.DarkThemeConfig
import com.xeladevmobile.medicalassistant.core.model.data.ThemeBrand
import com.xeladevmobile.medicalassistant.core.model.data.UserData
import com.xeladevmobile.medicalassistant.core.model.data.UserType
import kotlinx.coroutines.flow.Flow

interface UserDataRepository {

    /**
     * Stream of [UserData]
     */
    val userData: Flow<UserData>

    /**
     * Sets the desired theme brand.
     */
    suspend fun setThemeBrand(themeBrand: ThemeBrand)

    /**
     * Sets the desired dark theme config.
     */
    suspend fun setDarkThemeConfig(darkThemeConfig: DarkThemeConfig)

    /**
     * Sets the preferred dynamic color config.
     */
    suspend fun setDynamicColorPreference(useDynamicColor: Boolean)

    /**
     * Sets whether the user has completed the onboarding process.
     */
    suspend fun setShouldHideOnboarding(shouldHideOnboarding: Boolean)

    /**
     * Sets the user's full name.
     */
    suspend fun setFullName(fullName: String)

    /**
     * Sets the user's sex.
     */
    suspend fun setSex(sex: String)

    /**
     * Sets the user's birth date.
     */
    suspend fun setBornDate(bornDate: String)

    /**
     * Sets the user's address.
     */
    suspend fun setAddress(address: String)

    /**
     * Sets the user's type.
     */
    suspend fun setUserType(userType: UserType)

    /**
     * Sets the user's problem description.
     */
    suspend fun setProblemDescription(problemDescription: String)

    /**
     * Sets the user's treatment date.
     */
    suspend fun setTreatmentDate(treatmentDate: String)

    /**
     * Sets the user's specialty.
     */
    suspend fun setSpecialty(specialty: String)

    /**
     * Sets the user's graduation date.
     */
    suspend fun setGraduationDate(graduationDate: String)

    /**
     * Sets the user's experience.
     */
    suspend fun setExperience(experience: String)

    /**
     * Sets the user's occupation.
     */
    suspend fun setOccupation(occupation: String)

    suspend fun setUserFromNetwork(userData: UserData)
}
