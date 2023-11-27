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

package com.xeladevmobile.medicalassistant.core.network.retrofit

import com.jakewharton.retrofit2.converter.kotlinx.serialization.asConverterFactory
import com.xeladevmobile.medicalassistant.core.common.result.Result
import com.xeladevmobile.medicalassistant.core.network.BuildConfig
import com.xeladevmobile.medicalassistant.core.network.MedicalNetworkDataSource
import com.xeladevmobile.medicalassistant.core.network.model.NetworkDetailedResponse
import com.xeladevmobile.medicalassistant.core.network.model.NetworkSimplePrediction
import kotlinx.serialization.SerializationException
import kotlinx.serialization.json.Json
import okhttp3.Call
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.MultipartBody
import okhttp3.RequestBody.Companion.asRequestBody
import retrofit2.HttpException
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.scalars.ScalarsConverterFactory
import retrofit2.http.Multipart
import retrofit2.http.POST
import retrofit2.http.Part
import java.io.File
import java.io.IOException
import java.net.UnknownHostException
import javax.inject.Inject
import javax.inject.Singleton

/**
 * Retrofit API declaration for Medical Network API
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
    ): Response<NetworkDetailedResponse>

    @Multipart
    @POST("predict/emotion")
    suspend fun predictEmotion(
        @Part file: MultipartBody.Part,
    ): String

    @Multipart
    @POST("predict/emotion/percents")
    suspend fun predictEmotionPercents(
        @Part file: MultipartBody.Part,
    ): Response<NetworkDetailedResponse>
}

private const val MEDICAL_BASE_URL = BuildConfig.BACKEND_URL

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

    override suspend fun predictGender(file: File): Result<NetworkSimplePrediction> {
        return safeApiCallForString {
            val prediction = networkApi.predictGender(file.toMultipartBodyPart())
            NetworkSimplePrediction(prediction)
        }
    }

    override suspend fun predictGenderPercents(file: File): Result<NetworkDetailedResponse> {
        return safeApiCall {
            networkApi.predictGenderPercents(file.toMultipartBodyPart())
        }
    }

    override suspend fun predictEmotion(file: File): Result<NetworkSimplePrediction> {
        return safeApiCallForString {
            val prediction = networkApi.predictEmotion(file.toMultipartBodyPart())
            NetworkSimplePrediction(prediction)
        }
    }

    override suspend fun predictEmotionPercents(file: File): Result<NetworkDetailedResponse> {
        return safeApiCall {
            networkApi.predictEmotionPercents(file.toMultipartBodyPart())
        }
    }

    private fun File.toMultipartBodyPart(): MultipartBody.Part {
        val requestFile = this.asRequestBody("audio/*".toMediaType())
        return MultipartBody.Part.createFormData(
            "file",
            name,
            requestFile,
        )
    }

    private suspend fun <T> safeApiCall(call: suspend () -> Response<T>): Result<T> {
        return try {
            val response = call()
            if (response.isSuccessful) {
                response.body()?.let { Result.Success(it) }
                    ?: Result.Error(Exception("Response body is null"))
            } else {
                Result.Error(HttpException(response))
            }
        } catch (e: HttpException) {
            // Handle HTTP exceptions here. You can parse the response body to get more details, if the API supports it
            Result.Error(e)
        } catch (e: UnknownHostException) {
            // Handle network exceptions here
            Result.Error(e)
        } catch (e: IOException) {
            // Handle other IO exceptions here
            Result.Error(e)
        } catch (e: SerializationException) {
            // Handle JSON parsing exceptions here
            Result.Error(e)
        }
    }
}

suspend fun <String> safeApiCallForString(call: suspend () -> String): Result<String> {
    return try {
        Result.Success(call())
    } catch (e: Exception) {
        // Handle exceptions as you would normally
        Result.Error(e)
    }
}
