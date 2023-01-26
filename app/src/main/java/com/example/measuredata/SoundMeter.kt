package com.example.measuredata

import android.Manifest
import android.app.Activity
import android.content.Context
import android.content.pm.PackageManager
import android.media.AudioFormat
import android.media.AudioRecord
import android.media.MediaRecorder
import android.util.Log
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import java.io.IOException
import java.lang.Math.abs
import java.lang.Math.max
import kotlin.math.log10
import kotlin.math.sqrt

class SoundMeter(private val context: Context) {
    private val mediaRecorder = MediaRecorder()
    private var recording = false
    private var maxDb = 0f

    init {
        mediaRecorder.setAudioSource(MediaRecorder.AudioSource.MIC)
        mediaRecorder.setOutputFormat(MediaRecorder.OutputFormat.THREE_GPP)
        mediaRecorder.setAudioEncoder(MediaRecorder.AudioEncoder.AMR_NB)
        mediaRecorder.setOutputFile("/dev/fun")
    }

    fun startRecording() {
        try {
            mediaRecorder.prepare()
            mediaRecorder.start()
            recording = true
        } catch (e: IllegalStateException) {
            e.printStackTrace()
        } catch (e: IOException) {
            e.printStackTrace()
        }
    }

    fun stopRecording() {
        if (recording) {
            mediaRecorder.stop()
            mediaRecorder.release()
            recording = false
        }
    }

    fun getDecibelValue(): Float {
        if (recording) {
            val amplitude = mediaRecorder.maxAmplitude.toDouble()
            val db = 20 * log10(amplitude)
            Log.d(TAG, "dB1 update: $db")
            maxDb = maxDb.coerceAtLeast(db.toFloat())
            Log.d(TAG, "dB2 update: $maxDb")
            return maxDb.toFloat()
        } else {
            return 0f
        }
    }
}


