/*
 * Copyright 2021 The Android Open Source Project
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

package com.example.measuredata

import android.content.Context
import android.os.StrictMode
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import androidx.health.services.client.data.DataTypeAvailability
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import org.json.JSONObject
import java.io.DataOutputStream
import java.net.HttpURLConnection
import java.net.URL
import javax.inject.Inject
import com.example.measuredata.SoundMeter

/**
 * Holds most of the interaction logic and UI state for the app.
 */



var currentHeartRate = 0
fun updateHeartRate(newHeartRate: Double) {
    currentHeartRate = newHeartRate.toInt()
}

fun getHeartRate(): Int {
    return currentHeartRate
}
@HiltViewModel
class MainViewModel @Inject constructor(
    private val healthServicesManager: HealthServicesManager
) : ViewModel() {

    private val _uiState = MutableStateFlow<UiState>(UiState.Startup)
    val uiState: StateFlow<UiState> = _uiState

    private val _heartRateAvailable = MutableStateFlow(DataTypeAvailability.UNKNOWN)
    val heartRateAvailable: StateFlow<DataTypeAvailability> = _heartRateAvailable

    private val _heartRateBpm = MutableStateFlow(0.0)
    val heartRateBpm: StateFlow<Double> = _heartRateBpm
    val currentHeartRate: Double = _heartRateBpm.value
    val currentHeartRateInt: Int = currentHeartRate.toInt()

    init {
        // Check that the device has the heart rate capability and progress to the next state
        // accordingly.
        viewModelScope.launch {
            _uiState.value = if (healthServicesManager.hasHeartRateCapability()) {
                UiState.HeartRateAvailable
            } else {
                UiState.HeartRateNotAvailable
            }
        }
    }
    @ExperimentalCoroutinesApi
    suspend fun measureHeartRate() {
        healthServicesManager.heartRateMeasureFlow().collect {
            when (it) {
                is MeasureMessage.MeasureAvailability -> {
                    Log.d(TAG, "Availability changed: ${it.availability}")
                    _heartRateAvailable.value = it.availability
                }
                is MeasureMessage.MeasureData -> {
                    val bpm = it.data.last().value
                    Log.d(TAG, "Data update: $bpm")
                    _heartRateBpm.value = bpm
                    updateHeartRate(bpm)
                }
            }

        }

    }

}

sealed class UiState {
    object Startup : UiState()
    object HeartRateAvailable : UiState()
    object HeartRateNotAvailable : UiState()
}

fun sendData(amp: Float) {
//org all the data and ensure it is ready to be sent
    //HR
    val intHR = getHeartRate()
    //http post start
    val policy = StrictMode.ThreadPolicy.Builder().permitAll().build()
    StrictMode.setThreadPolicy(policy)
    val url = URL("https://smart-sense-dashboard.herokuapp.com/api/sensors/")
    val urlConnection = url.openConnection() as HttpURLConnection

// Set the request method to POST
    urlConnection.requestMethod = "POST"

// Set the request body as a JSON object
    val jsonObject = JSONObject()
    jsonObject.put("device_id", "Andy")
    jsonObject.put("decibel_reading", amp)
    jsonObject.put("heartrate", intHR)
    jsonObject.put("coord_latitude", 0f)
    jsonObject.put("coord_longitude", 0f)
    urlConnection.doOutput = true
    urlConnection.setRequestProperty("Content-Type", "application/json")

// Set the authorization token
    val token = "4a16b669-43bc-412a-8dad-18dc7fb199d9"
    urlConnection.setRequestProperty("Authorization", "Bearer $token")

    val outputWriter = DataOutputStream(urlConnection.outputStream)
    outputWriter.write(jsonObject.toString().toByteArray())
    outputWriter.flush()
    outputWriter.close()

// Read the response
    val responseCode = urlConnection.responseCode
    if (responseCode == 200) {
        val inputStream = urlConnection.inputStream
        val response = inputStream.bufferedReader().use { it.readText() }
        println(response)
    }
}




