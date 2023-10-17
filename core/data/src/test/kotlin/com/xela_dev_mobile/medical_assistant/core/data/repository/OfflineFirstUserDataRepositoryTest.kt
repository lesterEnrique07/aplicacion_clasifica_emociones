/*
 * Copyright 2022 The Android Open Source Project
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

package com.xela_dev_mobile.medical_assistant.core.data.repository

import com.google.samples.apps.nowinandroid.core.datastore.test.testUserPreferencesDataStore
import com.xela_dev_mobile.medical_assistant.core.analytics.NoOpAnalyticsHelper
import com.xela_dev_mobile.medical_assistant.core.datastore.MedicalPreferencesDataSource
import com.xela_dev_mobile.medical_assistant.core.model.data.DarkThemeConfig
import com.xela_dev_mobile.medical_assistant.core.model.data.ThemeBrand
import com.xela_dev_mobile.medical_assistant.core.model.data.UserData
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.test.TestScope
import kotlinx.coroutines.test.UnconfinedTestDispatcher
import kotlinx.coroutines.test.runTest
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.rules.TemporaryFolder
import kotlin.test.assertEquals
import kotlin.test.assertFalse
import kotlin.test.assertTrue

class OfflineFirstUserDataRepositoryTest {

    private val testScope = TestScope(UnconfinedTestDispatcher())

    private lateinit var subject: OfflineFirstUserDataRepository

    private lateinit var medicalPreferencesDataSource: MedicalPreferencesDataSource

    private val analyticsHelper = NoOpAnalyticsHelper()

    @get:Rule
    val tmpFolder: TemporaryFolder = TemporaryFolder.builder().assureDeletion().build()

    @Before
    fun setup() {
        medicalPreferencesDataSource = MedicalPreferencesDataSource(
            tmpFolder.testUserPreferencesDataStore(testScope),
        )

        subject = OfflineFirstUserDataRepository(
            medicalPreferencesDataSource = medicalPreferencesDataSource,
            analyticsHelper,
        )
    }

    @Test
    fun offlineFirstUserDataRepository_default_user_data_is_correct() =
        testScope.runTest {
            assertEquals(
                UserData(
                    themeBrand = ThemeBrand.DEFAULT,
                    darkThemeConfig = DarkThemeConfig.FOLLOW_SYSTEM,
                    useDynamicColor = false,
                    shouldHideOnboarding = false,
                ),
                subject.userData.first(),
            )
        }

    @Test
    fun offlineFirstUserDataRepository_set_theme_brand_delegates_to_medicalassist_preferences() =
        testScope.runTest {
            subject.setThemeBrand(ThemeBrand.ANDROID)

            assertEquals(
                ThemeBrand.ANDROID,
                subject.userData
                    .map { it.themeBrand }
                    .first(),
            )
            assertEquals(
                ThemeBrand.ANDROID,
                medicalPreferencesDataSource
                    .userData
                    .map { it.themeBrand }
                    .first(),
            )
        }

    @Test
    fun offlineFirstUserDataRepository_set_dynamic_color_delegates_to_medicalassist_preferences() =
        testScope.runTest {
            subject.setDynamicColorPreference(true)

            assertEquals(
                true,
                subject.userData
                    .map { it.useDynamicColor }
                    .first(),
            )
            assertEquals(
                true,
                medicalPreferencesDataSource
                    .userData
                    .map { it.useDynamicColor }
                    .first(),
            )
        }

    @Test
    fun offlineFirstUserDataRepository_set_dark_theme_config_delegates_to_medicalassist_preferences() =
        testScope.runTest {
            subject.setDarkThemeConfig(DarkThemeConfig.DARK)

            assertEquals(
                DarkThemeConfig.DARK,
                subject.userData
                    .map { it.darkThemeConfig }
                    .first(),
            )
            assertEquals(
                DarkThemeConfig.DARK,
                medicalPreferencesDataSource
                    .userData
                    .map { it.darkThemeConfig }
                    .first(),
            )
        }

    @Test
    fun whenUserCompletesOnboarding_thenRemovesAllInterests_shouldHideOnboardingIsFalse() =
        testScope.runTest {
            subject.setShouldHideOnboarding(true)
            assertTrue(subject.userData.first().shouldHideOnboarding)

            assertFalse(subject.userData.first().shouldHideOnboarding)
        }
}
