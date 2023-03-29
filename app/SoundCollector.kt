import android.Manifest
import android.annotation.SuppressLint
import android.app.Activity
import android.content.Context
import android.content.pm.PackageManager
import android.media.AudioFormat
import android.media.AudioRecord
import android.media.MediaRecorder
import android.os.Process
import androidx.core.content.ContextCompat
import androidx.core.app.ActivityCompat





@SuppressLint("MissingPermission")
class SoundCollector(private val context: Context) {

    private val audioRecord: AudioRecord
    private var bufferSize: Int = 0
    private var soundLevel: Float = 0.0f

    init {
        bufferSize = AudioRecord.getMinBufferSize(44100, AudioFormat.CHANNEL_IN_MONO, AudioFormat.ENCODING_PCM_16BIT)
        audioRecord = AudioRecord(
            MediaRecorder.AudioSource.MIC,
            44100,
            AudioFormat.CHANNEL_IN_MONO,
            AudioFormat.ENCODING_PCM_16BIT,
            bufferSize
        )
    }

    fun startCollectingSound() {
        if (ContextCompat.checkSelfPermission(context, Manifest.permission.RECORD_AUDIO) != PackageManager.PERMISSION_GRANTED) {
            // Request permission from the user
            ActivityCompat.requestPermissions(context as Activity, arrayOf(Manifest.permission.RECORD_AUDIO), 1)
            return
        }

        try {
            audioRecord.startRecording()

            val buffer = ShortArray(bufferSize)
            while (true) {
                val bufferReadResult = audioRecord.read(buffer, 0, bufferSize)
                val max = buffer.iterator().max() ?: 0
                soundLevel = max.toFloat() / 32768 * 100
                // You can do something with soundLevel here
                // For example, update it in MainViewModel
            }
        } catch (e: SecurityException) {
            // Handle the exception
        }
    }




    fun stopCollectingSound() {
        audioRecord.stop()
        audioRecord.release()
    }

    fun getSoundLevel(): Float {
        return soundLevel
    }
}
