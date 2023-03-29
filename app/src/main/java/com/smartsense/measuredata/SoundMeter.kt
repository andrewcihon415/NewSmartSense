package com.smartsense.measuredata

import android.annotation.SuppressLint
import android.media.AudioRecord
import android.media.MediaRecorder
import kotlin.math.log10
//class to get the max amplitude of the microphone and convert it to decibels then pass to the main activity

class SoundMeter(private val sampleRate: Int, private val channelConfig: Int, private val audioFormat: Int) {
    private val bufferSize = AudioRecord.getMinBufferSize(sampleRate, channelConfig, audioFormat)
    @SuppressLint("MissingPermission")
    private val audioRecord = AudioRecord(MediaRecorder.AudioSource.MIC, sampleRate, channelConfig, audioFormat, bufferSize)
    //permission check is present, just not here...intone it for now
    fun startRecording() { //needs to have permission check before call, which is done in MainActivity
        audioRecord.startRecording()
    }

    fun stopRecording() { //needs to be called after 10 second collecting period to get most recent max value
        audioRecord.stop()
    }

    fun getDecibelValue(): Float { //returns the max amplitude in decibels
        val buffer = ShortArray(bufferSize)
        audioRecord.read(buffer, 0, bufferSize)
        var maxAmplitude = 0 //max amplitude in the buffer
        for (i in buffer.indices) { //finds the max amplitude in the buffer by iterating through the buffer
            maxAmplitude = maxAmplitude.coerceAtLeast(kotlin.math.abs(buffer[i].toInt()))
        }
        return 20 * log10(maxAmplitude.toDouble()).toFloat() //math to be changed for calibration(to nearest whole number)
    }
}



