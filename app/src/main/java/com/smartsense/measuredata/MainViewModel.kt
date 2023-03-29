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

package com.smartsense.measuredata


import android.app.Activity
import android.app.PendingIntent.getActivity
import android.content.Context
import android.graphics.Color
import android.media.AudioManager
import android.media.ToneGenerator
import android.os.Build
import android.os.StrictMode
import android.os.Vibrator
import android.util.Log
import android.widget.TextView
import androidx.core.content.ContextCompat.getSystemService
import androidx.health.services.client.data.DataTypeAvailability
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import okhttp3.*
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.RequestBody.Companion.toRequestBody
import org.json.JSONObject
import java.io.DataOutputStream
import java.io.IOException
import java.net.HttpURLConnection
import java.net.URL
import javax.inject.Inject


/**
 * Holds most of the interaction logic and UI state for the app.
 */
var userID: Int? = null
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
var userName = getDeviceName() //set the userName to the device name
var organization =1 //will come later, for now it is set to 1 (SmartSense)
fun createUserName() {
    Log.d("DeviceName", userName)
    val client = OkHttpClient()
    val request = Request.Builder()
        .url("https://smart-sense-dashboard.herokuapp.com/api/users/$userName/")
        .addHeader("Authorization", "4a16b669-43bc-412a-8dad-18dc7fb199d9")
        .addHeader("Content-Type", "application/json")
        .build()

    client.newCall(request).enqueue(object : Callback {
        override fun onFailure(call: Call, e: IOException) {
            // Handle failure
        }

        override fun onResponse(call: Call, response: Response) {
            if (response.code == 404) {


                // Build JSON request body
                val json = """
                {
                    "name": "$userName",
                    "organization": $organization
                }
                """

                // Make a new request with the userName and organization
                val request = Request.Builder()
                    .url("https://smart-sense-dashboard.herokuapp.com/api/users/")
                    .addHeader("Authorization", "4a16b669-43bc-412a-8dad-18dc7fb199d9")
                    .addHeader("Content-Type", "application/json")
                    .post(json.toRequestBody("application/json; charset=utf-8".toMediaTypeOrNull()))
                    .build()

                client.newCall(request).enqueue(object : Callback {
                    override fun onFailure(call: Call, e: IOException) {
                        //handle failure
                    }

                    override fun onResponse(call: Call, response: Response) {
                        val jsonResponse = JSONObject(response.body!!.string())
                        Log.d("userMake", jsonResponse.toString())
                    }
                })
            }
        }
    })
}
fun getUserID() {
    val client = OkHttpClient()
    val request = Request.Builder()
        .url("https://smart-sense-dashboard.herokuapp.com/api/users/$userName/")
        .addHeader("Authorization", "4a16b669-43bc-412a-8dad-18dc7fb199d9")
        .addHeader("Content-Type", "application/json")
        .get()
        .build()

    client.newCall(request).enqueue(object : Callback {
        override fun onFailure(call: Call, e: IOException) {
            // Handle failure
        }

        override fun onResponse(call: Call, response: Response) {
            val jsonResponse = JSONObject(response.body!!.string())
            Log.d("userID", jsonResponse.toString())
            userID = jsonResponse.getInt("id")
            Log.d("userID2", userID.toString())
        }
    })
}

fun sendData(amp: Float,lat: Float, lng: Float) {

//org all the data and ensure it is ready to be sent
    //HR
    val intHR = getHeartRate()
    //log lat, lng and dB data
    Log.d("lat", lat.toString())
    Log.d("lng", lng.toString())
    Log.d("dB", amp.toString())
    //http post start
    val policy = StrictMode.ThreadPolicy.Builder().permitAll().build()
    StrictMode.setThreadPolicy(policy)
    val url = URL("https://smart-sense-dashboard.herokuapp.com/api/sensors/")
    val urlConnection = url.openConnection() as HttpURLConnection

// Set the request method to POST
    urlConnection.requestMethod = "POST"

// Set the request body as a JSON object (will need updated with new push of backend)
    val jsonObject = JSONObject()
    jsonObject.put("device_id", userName) //update with device id (string)
    jsonObject.put("decibel_reading", amp)
    jsonObject.put("heartrate", intHR)
    jsonObject.put("coord_latitude", lat)
    jsonObject.put("coord_longitude", lng)
    jsonObject.put("user", userID)//update with userID
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
fun changeScreenColor(HR: Int, gas: Float,sound : Float, myTextViewHR : TextView, myTextViewGas : TextView, myTextViewSound : TextView) {
    when {
                HR > 120 -> {
                    myTextViewHR.setTextColor(Color.RED)
                }
                gas > 2000 -> {
                    myTextViewGas.setTextColor(Color.RED)
                }
                sound > 60 -> {
                    myTextViewSound.setTextColor(Color.RED)
                }
                else -> {
                    myTextViewHR.setTextColor(Color.GREEN)
                    myTextViewGas.setTextColor(Color.GREEN)
                    myTextViewSound.setTextColor(Color.GREEN)
                }
            }
}

fun getDeviceName(): String {
    val manufacturer = Build.MANUFACTURER
    val model = Build.MODEL
    return if (model.startsWith(manufacturer)) {
        capitalize(model)
    } else {
        capitalize(manufacturer) + " " + model
    }
}

fun capitalize(str: String?): String {
    if (str == null || str.isEmpty()) {
        return ""
    }
    val first = str[0]
    return if (Character.isUpperCase(first)) {
        str
    } else {
        Character.toUpperCase(first) + str.substring(1)
    }
}





