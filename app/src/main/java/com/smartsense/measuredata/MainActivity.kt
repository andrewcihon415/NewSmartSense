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

import android.annotation.SuppressLint
import android.bluetooth.*
import android.content.Context
import android.content.pm.PackageManager
import android.location.Location
import android.location.LocationListener
import android.location.LocationManager
import android.media.AudioFormat
import android.os.*
import android.util.Log
import android.view.WindowManager
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.core.view.isVisible
import androidx.lifecycle.lifecycleScope
import com.smartsense.measuredata.databinding.ActivityMainBinding
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.ExperimentalCoroutinesApi
import java.util.*
import android.bluetooth.BluetoothGatt
import android.bluetooth.BluetoothGattCallback
import android.bluetooth.BluetoothGattCharacteristic
import android.bluetooth.BluetoothManager
import android.bluetooth.BluetoothProfile
import android.widget.TextView


/**
 * Activity displaying the app UI. Notably, this binds data from [MainViewModel] to views on screen,
 * and performs the permission check when enabling measure data.
 */

@AndroidEntryPoint
class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding
    private lateinit var permissionLauncher: ActivityResultLauncher<String>
    private lateinit var handlerThread: HandlerThread
    private lateinit var handler: Handler
    private lateinit var soundMeter: SoundMeter
    var soundLevel: Float = 0f //default value that will be returned for first 10 seconds
    private val viewModel: MainViewModel by viewModels()
    var lat = 0f//default value that will be returned until location is found
    var lng = 0f
    var potValue = 0.0f

    @OptIn(ExperimentalCoroutinesApi::class)
    override fun onCreate(savedInstanceState: Bundle?) { //done on every app start

        getUserID()
        super.onCreate(savedInstanceState)
        //location collection
        val locationManager = getSystemService(Context.LOCATION_SERVICE) as LocationManager
        val locationListener = object : LocationListener {
            override fun onLocationChanged(location: Location) {
                lat = location.latitude.toFloat()
                lng = location.longitude.toFloat()

            }

            @Deprecated("Deprecated in Java") //should not cause to many issues for now
            override fun onStatusChanged(provider: String?, status: Int, extras: Bundle?) {
            }

            override fun onProviderEnabled(provider: String) {}
            override fun onProviderDisabled(provider: String) {}
        }
        if (ContextCompat.checkSelfPermission(
                this,
                android.Manifest.permission.ACCESS_FINE_LOCATION
            )
            != PackageManager.PERMISSION_GRANTED
        ) {
            ActivityCompat.requestPermissions(
                this,
                arrayOf(android.Manifest.permission.ACCESS_FINE_LOCATION),
                1
            )
        } else {
            locationManager.requestLocationUpdates(
                LocationManager.GPS_PROVIDER,
                0,
                0f,
                locationListener
            )
        }
        window.addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON)
        soundMeter = SoundMeter(
            8000,
            AudioFormat.CHANNEL_IN_MONO,
            AudioFormat.ENCODING_PCM_16BIT
        )//recording settings
        if (ContextCompat.checkSelfPermission(
                this,
                android.Manifest.permission.RECORD_AUDIO
            ) != PackageManager.PERMISSION_GRANTED
        ) {
            ActivityCompat.requestPermissions(
                this,
                arrayOf(android.Manifest.permission.RECORD_AUDIO),
                1
            )
        } else {
            soundMeter.startRecording()//permission check done here
        }
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        handlerThread = HandlerThread("name")
        handlerThread.start()
        handler = Handler(handlerThread.looper)

        permissionLauncher =
            registerForActivityResult(ActivityResultContracts.RequestPermission()) { result ->
                when (result) {
                    true -> {
                        Log.i(TAG, "Body sensors permission granted")
                        // Only measure while the activity is at least in STARTED state.
                        // MeasureClient provides frequent updates, which requires increasing the
                        // sampling rate of device sensors, so we must be careful not to remain
                        // registered any longer than necessary.
                        lifecycleScope.launchWhenStarted {
                            viewModel.measureHeartRate()
                        }
                    }
                    false -> Log.i(TAG, "Body sensors permission not granted")
                }
            }

        // Bind viewmodel state to the UI.
        lifecycleScope.launchWhenStarted {
            viewModel.uiState.collect {
                updateViewVisiblity(it)
            }

        }
        lifecycleScope.launchWhenStarted {
            viewModel.heartRateBpm.collect {
                binding.lastMeasuredValue.text = String.format("%.1f", it)

            }
        }


    }

    override fun onStart() { //ask for permissions on first start
        super.onStart()
        permissionLauncher.launch(android.Manifest.permission.BODY_SENSORS)
        permissionLauncher.launch(android.Manifest.permission.RECORD_AUDIO)
        permissionLauncher.launch(android.Manifest.permission.ACCESS_FINE_LOCATION)
        permissionLauncher.launch(android.Manifest.permission.ACCESS_COARSE_LOCATION)
    }


    private fun updateViewVisiblity(uiState: UiState) {
        (uiState is UiState.Startup).let {
            binding.progress.isVisible = it
        }
        // These views are visible when the capability is available.
        (uiState is UiState.HeartRateAvailable).let {
            binding.lastMeasuredValue.isVisible = it
            binding.heart.isVisible = it
        }
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
            soundMeter.startRecording()
        }

    }

    override fun onResume() {
        super.onResume()
        startTimer()
    }

    override fun onPause() {
        super.onPause()
        stopTimer()
        super.onPause()

    }


    private fun startTimer() {
        val runit = object : Runnable {
            override fun run() {
                soundMeter.startRecording()
                Thread.sleep(10000)//wait time for soundMeter to get value
                soundLevel = soundMeter.getDecibelValue()//get soundMeter value
                Log.d(TAG, "dB2 update: $soundLevel")
                soundMeter.stopRecording()//stop soundMeter
                val floatTextView = findViewById<TextView>(R.id.soundValue)
                floatTextView.text = soundLevel.toString()
                handler.postDelayed(this, 10000)//repeat every 10 seconds
            }
        }
        handler.postDelayed(runit, 10000)
        val runnable = object : Runnable {
            override fun run() {
                readPotValueFromBleDevice("70:04:1D:AD:82:2D",this@MainActivity) { potValue ->
                    Log.d("BLE Output", "Pot Value: $potValue")
                    this@MainActivity.potValue = potValue
                    val floatTextView = findViewById<TextView>(R.id.gasValue)
                    floatTextView.text = potValue.toString()
                }
                sendData(soundLevel, lat, lng)//send data to server
                runOnUiThread {
                    changeScreenColor(
                        currentHeartRate,
                        potValue,
                        soundLevel,
                        findViewById(R.id.last_measured_value),
                        findViewById(R.id.gasValue),
                        findViewById(R.id.soundValue))
                }
                handler.postDelayed(this, 1000)//repeat every 1 second
            }
        }
        handler.postDelayed(runnable, 1000)
    }

    private fun stopTimer() {
        handler.removeCallbacksAndMessages(null)
    }
}



@SuppressLint("MissingPermission")
fun readPotValueFromBleDevice(deviceAddress: String, context: Context, potValueCallback: (Float) -> Unit) {
    val bluetoothManager = context.getSystemService(Context.BLUETOOTH_SERVICE) as BluetoothManager
    val bluetoothAdapter = bluetoothManager.adapter
    val bluetoothDevice = bluetoothAdapter.getRemoteDevice(deviceAddress)
    val POT_CHARACTERISTIC_UUID = UUID.fromString("beb5483e-36e1-4688-b7f5-ea07361b26a8")
    val POT_SERVICE_UUID = UUID.fromString("4fafc201-1fb5-459e-8fcc-c5c9c331914b")
    val CLIENT_CHARACTERISTIC_CONFIG_UUID = UUID.fromString("00002902-0000-1000-8000-00805f9b34fb")
    val bluetoothGatt = bluetoothDevice.connectGatt(context, false, object : BluetoothGattCallback() {
        override fun onConnectionStateChange(gatt: BluetoothGatt?, status: Int, newState: Int) {
            super.onConnectionStateChange(gatt, status, newState)
            if (newState == BluetoothProfile.STATE_CONNECTED) {
                gatt?.discoverServices()
            }
        }

        override fun onServicesDiscovered(gatt: BluetoothGatt?, status: Int) {
            super.onServicesDiscovered(gatt, status)
            if (status == BluetoothGatt.GATT_SUCCESS) {
                val characteristic = gatt?.getService(POT_SERVICE_UUID)?.getCharacteristic(POT_CHARACTERISTIC_UUID)
                characteristic?.let {
                    gatt.setCharacteristicNotification(it, true)
                    val descriptor = it.getDescriptor(CLIENT_CHARACTERISTIC_CONFIG_UUID)
                    descriptor?.let {
                        it.value = BluetoothGattDescriptor.ENABLE_NOTIFICATION_VALUE
                        gatt.writeDescriptor(it)
                    }
                }
            }
        }

        override fun onDescriptorWrite(gatt: BluetoothGatt?, descriptor: BluetoothGattDescriptor?, status: Int) {
            super.onDescriptorWrite(gatt, descriptor, status)
            if (status == BluetoothGatt.GATT_SUCCESS) {
                val characteristic = gatt?.getService(POT_SERVICE_UUID)?.getCharacteristic(POT_CHARACTERISTIC_UUID)
                gatt?.readCharacteristic(characteristic)
            }
        }

        override fun onCharacteristicRead(gatt: BluetoothGatt?, characteristic: BluetoothGattCharacteristic?, status: Int) {
            super.onCharacteristicRead(gatt, characteristic, status)
            if (status == BluetoothGatt.GATT_SUCCESS && characteristic?.uuid == POT_CHARACTERISTIC_UUID) {
                val potValue =
                    characteristic?.getFloatValue(BluetoothGattCharacteristic.FORMAT_FLOAT, 0)
                if (potValue != null) {
                    potValueCallback(potValue)
                }
                gatt?.close()
            }
        }
    })
}


