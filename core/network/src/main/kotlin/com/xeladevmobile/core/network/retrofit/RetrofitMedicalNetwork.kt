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

package com.xeladevmobile.core.network.retrofit

import com.jakewharton.retrofit2.converter.kotlinx.serialization.asConverterFactory
import com.xeladevmobile.core.network.MedicalNetworkDataSource
import com.xeladevmobile.core.network.model.NetworkDetailedResponse
import com.xeladevmobile.core.network.model.NetworkSimplePrediction
import com.xeladevmobile.medicalassistant.core.network.BuildConfig
import kotlinx.serialization.Serializable
import kotlinx.serialization.json.Json
import okhttp3.Call
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.MultipartBody
import okhttp3.RequestBody
import retrofit2.Retrofit
import retrofit2.converter.scalars.ScalarsConverterFactory
import retrofit2.http.Multipart
import retrofit2.http.POST
import retrofit2.http.Part
import java.io.File
import javax.inject.Inject
import javax.inject.Singleton

/**
 * Retrofit API declaration for NIA Network API
 */
private interface RetrofitNiaNetworkApi {
    @Multipart
    @POST("predict/gender")
    suspend fun predictGender(
        @Part file: MultipartBody.Part,
    ): String

    @Multipart
    @POST("predict/gender/percents")
    suspend fun predictGenderPercents(
        @Part file: MultipartBody.Part,
    ): NetworkResponse<NetworkDetailedResponse>

    @Multipart
    @POST("predict/emotion")
    suspend fun predictEmotion(
        @Part file: MultipartBody.Part,
    ): String

    @Multipart
    @POST("predict/emotion/percents")
    suspend fun predictEmotionPercents(
        @Part file: MultipartBody.Part,
    ): NetworkResponse<NetworkDetailedResponse>
}

private const val MEDICAL_BASE_URL = BuildConfig.BACKEND_URL

/**
 * Wrapper for data provided from the [MEDICAL_BASE_URL]
 */
@Serializable
private data class NetworkResponse<T>(
    val data: T,
)

/**
 * [Retrofit] backed [MedicalNetworkDataSource]
 */
@Singleton
class RetrofitMedicalNetwork @Inject constructor(
    networkJson: Json,
    okhttpCallFactory: Call.Factory,
) : MedicalNetworkDataSource {

    private val networkApi = Retrofit.Builder()
        .baseUrl(MEDICAL_BASE_URL)
        .callFactory(okhttpCallFactory)
        .addConverterFactory(ScalarsConverterFactory.create())
        .addConverterFactory(
            networkJson.asConverterFactory("application/json".toMediaType()),
        )
        .build()
        .create(RetrofitNiaNetworkApi::class.java)

    override suspend fun predictGender(file: File): NetworkSimplePrediction {
        val prediction = networkApi.predictGender(file.toMultipartBodyPart())
        return NetworkSimplePrediction(prediction)
    }

    override suspend fun predictGenderPercents(file: File): NetworkDetailedResponse {
        return networkApi.predictGenderPercents(file.toMultipartBodyPart()).data
    }

    override suspend fun predictEmotion(file: File): NetworkSimplePrediction {
        val prediction = networkApi.predictEmotion(file.toMultipartBodyPart())
        return NetworkSimplePrediction(prediction)
    }

    override suspend fun predictEmotionPercents(file: File): NetworkDetailedResponse {
        return networkApi.predictEmotionPercents(file.toMultipartBodyPart()).data
    }

    private fun File.toMultipartBodyPart(): MultipartBody.Part {
        val requestFile = RequestBody.create(
            "audio/*".toMediaType(),
            this,
        )
        return MultipartBody.Part.createFormData(
            "file",
            name,
            requestFile,
        )
    }
}
