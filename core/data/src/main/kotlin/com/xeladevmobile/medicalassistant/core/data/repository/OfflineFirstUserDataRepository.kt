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

import com.xeladevmobile.medicalassistant.core.analytics.AnalyticsHelper
import com.xeladevmobile.medicalassistant.core.datastore.MedicalPreferencesDataSource
import com.xeladevmobile.medicalassistant.core.model.data.DarkThemeConfig
import com.xeladevmobile.medicalassistant.core.model.data.ThemeBrand
import com.xeladevmobile.medicalassistant.core.model.data.UserData
import com.xeladevmobile.medicalassistant.core.model.data.UserType
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject


class OfflineFirstUserDataRepository @Inject constructor(
    private val medicalPreferencesDataSource: MedicalPreferencesDataSource,
    private val analyticsHelper: AnalyticsHelper,
) : UserDataRepository {

    override val userData: Flow<UserData> =
        medicalPreferencesDataSource.userData

    override suspend fun setThemeBrand(themeBrand: ThemeBrand) {
        medicalPreferencesDataSource.setThemeBrand(themeBrand)
        analyticsHelper.logThemeChanged(themeBrand.name)
    }

    override suspend fun setDarkThemeConfig(darkThemeConfig: DarkThemeConfig) {
        medicalPreferencesDataSource.setDarkThemeConfig(darkThemeConfig)
        analyticsHelper.logDarkThemeConfigChanged(darkThemeConfig.name)
    }

    override suspend fun setDynamicColorPreference(useDynamicColor: Boolean) {
        medicalPreferencesDataSource.setDynamicColorPreference(useDynamicColor)
        analyticsHelper.logDynamicColorPreferenceChanged(useDynamicColor)
    }

    override suspend fun setShouldHideOnboarding(shouldHideOnboarding: Boolean) {
        medicalPreferencesDataSource.setShouldHideOnboarding(shouldHideOnboarding)
        analyticsHelper.logOnboardingStateChanged(shouldHideOnboarding)
    }

    override suspend fun setFullName(fullName: String) {
        medicalPreferencesDataSource.setName(fullName)
    }

    override suspend fun setSex(sex: String) {
        medicalPreferencesDataSource.setSex(sex)
    }

    override suspend fun setBornDate(bornDate: String) {
        medicalPreferencesDataSource.setBornDate(bornDate)
    }

    override suspend fun setAddress(address: String) {
        medicalPreferencesDataSource.setAddress(address)
    }

    override suspend fun setUserType(userType: UserType) {
        medicalPreferencesDataSource.setUserType(userType)
    }

    override suspend fun setProblemDescription(problemDescription: String) {
        medicalPreferencesDataSource.setProblemDescription(problemDescription)
    }

    override suspend fun setTreatmentDate(treatmentDate: String) {
        medicalPreferencesDataSource.setTreatmentDate(treatmentDate)
    }

    override suspend fun setSpecialty(specialty: String) {
        medicalPreferencesDataSource.setSpecialty(specialty)
    }

    override suspend fun setGraduationDate(graduationDate: String) {
        medicalPreferencesDataSource.setGraduationDate(graduationDate)
    }

    override suspend fun setExperience(experience: String) {
        medicalPreferencesDataSource.setExperience(experience)
    }

    override suspend fun setOccupation(occupation: String) {
        medicalPreferencesDataSource.setOccupation(occupation)
    }
}
