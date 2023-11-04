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

package com.xeladevmobile.core.network.fake

import JvmUnitTestFakeAssetManager
import com.google.samples.apps.nowinandroid.core.network.fake.FakeAssetManager
import com.xeladevmobile.core.network.MedicalNetworkDataSource
import com.xeladevmobile.core.network.model.NetworkDetailedResponse
import com.xeladevmobile.core.network.model.NetworkSimplePrediction
import com.xeladevmobile.medicalassistant.core.network.Dispatcher
import com.xeladevmobile.medicalassistant.core.network.MedicalDispatchers.*
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.withContext
import kotlinx.serialization.ExperimentalSerializationApi
import kotlinx.serialization.json.Json
import kotlinx.serialization.json.decodeFromStream
import java.io.File
import javax.inject.Inject

/**
 * [MedicalNetworkDataSource] implementation that provides static news resources to aid development
 */
class FakeMedicalNetworkDataSource @Inject constructor(
    @Dispatcher(IO) private val ioDispatcher: CoroutineDispatcher,
    private val networkJson: Json,
    private val assets: FakeAssetManager = JvmUnitTestFakeAssetManager,
) : MedicalNetworkDataSource {


    companion object {
        private const val PATIENTS_ASSET = "patients.json"
    }

    @OptIn(ExperimentalSerializationApi::class)
    override suspend fun predictGender(file: File): NetworkSimplePrediction =
        withContext(ioDispatcher) {
            assets.open(PATIENTS_ASSET).use(networkJson::decodeFromStream)
        }

    @OptIn(ExperimentalSerializationApi::class)
    override suspend fun predictGenderPercents(file: File): NetworkDetailedResponse =
        withContext(ioDispatcher) {
            assets.open(PATIENTS_ASSET).use(networkJson::decodeFromStream)
        }

    @OptIn(ExperimentalSerializationApi::class)
    override suspend fun predictEmotion(file: File): NetworkSimplePrediction =
        withContext(ioDispatcher) {
            assets.open(PATIENTS_ASSET).use(networkJson::decodeFromStream)
        }

    @OptIn(ExperimentalSerializationApi::class)
    override suspend fun predictEmotionPercents(file: File): NetworkDetailedResponse =
        withContext(ioDispatcher) {
            assets.open(PATIENTS_ASSET).use(networkJson::decodeFromStream)
        }
}