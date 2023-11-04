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

package com.xeladevmobile.medicalassistant.core.datastore

import androidx.datastore.core.DataStore
import com.xeladevmobile.medicalassistant.core.datastore.UserTypeProto.DOCTOR
import com.xeladevmobile.medicalassistant.core.datastore.UserTypeProto.PATIENT
import com.xeladevmobile.medicalassistant.core.datastore.UserTypeProto.UNRECOGNIZED
import com.xeladevmobile.medicalassistant.core.model.data.DarkThemeConfig
import com.xeladevmobile.medicalassistant.core.model.data.ThemeBrand
import com.xeladevmobile.medicalassistant.core.model.data.UserData
import com.xeladevmobile.medicalassistant.core.model.data.UserType
import kotlinx.coroutines.flow.map
import javax.inject.Inject

class MedicalPreferencesDataSource @Inject constructor(
    private val userPreferences: DataStore<UserPreferences>,
) {
    val userData = userPreferences.data
        .map {
            UserData(
                themeBrand = when (it.themeBrand) {
                    null,
                    ThemeBrandProto.THEME_BRAND_UNSPECIFIED,
                    ThemeBrandProto.UNRECOGNIZED,
                    ThemeBrandProto.THEME_BRAND_DEFAULT,
                    -> ThemeBrand.DEFAULT

                    ThemeBrandProto.THEME_BRAND_ANDROID -> ThemeBrand.ANDROID
                },
                darkThemeConfig = when (it.darkThemeConfig) {
                    null,
                    DarkThemeConfigProto.DARK_THEME_CONFIG_UNSPECIFIED,
                    DarkThemeConfigProto.UNRECOGNIZED,
                    DarkThemeConfigProto.DARK_THEME_CONFIG_FOLLOW_SYSTEM,
                    ->
                        DarkThemeConfig.FOLLOW_SYSTEM

                    DarkThemeConfigProto.DARK_THEME_CONFIG_LIGHT ->
                        DarkThemeConfig.LIGHT

                    DarkThemeConfigProto.DARK_THEME_CONFIG_DARK -> DarkThemeConfig.DARK
                },
                useDynamicColor = it.useDynamicColor,
                shouldHideOnboarding = it.shouldHideOnboarding,
                userType = when (it.userType) {
                    DOCTOR -> UserType.DOCTOR
                    null, UNRECOGNIZED, PATIENT -> UserType.PATIENT
                },
                treatmentDate = it.treatmentDate,
                specialty = it.specialty,
                problemDescription = it.problemDescription,
                sex = it.sex,
                address = it.address,
                bornDate = it.bornDate,
                experience = it.experience,
                graduationDate = it.graduationDate,
                name = it.name,
                occupation = it.occupation,
            )
        }

    suspend fun setThemeBrand(themeBrand: ThemeBrand) {
        userPreferences.updateData {
            it.copy {
                this.themeBrand = when (themeBrand) {
                    ThemeBrand.DEFAULT -> ThemeBrandProto.THEME_BRAND_DEFAULT
                    ThemeBrand.ANDROID -> ThemeBrandProto.THEME_BRAND_ANDROID
                }
            }
        }
    }

    suspend fun setDynamicColorPreference(useDynamicColor: Boolean) {
        userPreferences.updateData {
            it.copy {
                this.useDynamicColor = useDynamicColor
            }
        }
    }

    suspend fun setDarkThemeConfig(darkThemeConfig: DarkThemeConfig) {
        userPreferences.updateData {
            it.copy {
                this.darkThemeConfig = when (darkThemeConfig) {
                    DarkThemeConfig.FOLLOW_SYSTEM ->
                        DarkThemeConfigProto.DARK_THEME_CONFIG_FOLLOW_SYSTEM

                    DarkThemeConfig.LIGHT -> DarkThemeConfigProto.DARK_THEME_CONFIG_LIGHT
                    DarkThemeConfig.DARK -> DarkThemeConfigProto.DARK_THEME_CONFIG_DARK
                }
            }
        }
    }

    suspend fun setShouldHideOnboarding(shouldHideOnboarding: Boolean) {
        userPreferences.updateData {
            it.copy {
                this.shouldHideOnboarding = shouldHideOnboarding
            }
        }
    }

    suspend fun setSex(sex: String) {
        userPreferences.updateData {
            it.copy {
                this.sex = sex
            }
        }
    }

    suspend fun setOccupation(occupation: String) {
        userPreferences.updateData {
            it.copy {
                this.occupation = occupation
            }
        }
    }

    suspend fun setBornDate(bornDate: String) {
        userPreferences.updateData {
            it.copy {
                this.bornDate = bornDate
            }
        }
    }

    suspend fun setExperience(experience: String) {
        userPreferences.updateData {
            it.copy {
                this.experience = experience
            }
        }
    }

    suspend fun setAddress(address: String) {
        userPreferences.updateData {
            it.copy {
                this.address = address
            }
        }
    }

    suspend fun setGraduationDate(graduationDate: String) {
        userPreferences.updateData {
            it.copy {
                this.graduationDate = graduationDate
            }
        }
    }

    suspend fun setName(name: String) {
        userPreferences.updateData {
            it.copy {
                this.name = name
            }
        }
    }

    suspend fun setProblemDescription(problemDescription: String) {
        userPreferences.updateData {
            it.copy {
                this.problemDescription = problemDescription
            }
        }
    }

    suspend fun setSpecialty(specialty: String) {
        userPreferences.updateData {
            it.copy {
                this.specialty = specialty
            }
        }
    }

    suspend fun setTreatmentDate(treatmentDate: String) {
        userPreferences.updateData {
            it.copy {
                this.treatmentDate = treatmentDate
            }
        }
    }


}
