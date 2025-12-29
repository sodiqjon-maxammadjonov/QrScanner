package uz.mukhammadsodikh.november.qrscanner.utils

import android.content.Context
import android.media.MediaPlayer
import android.media.RingtoneManager

/**
 * Sound Manager - Scan sounds
 */
object SoundManager {

    private var mediaPlayer: MediaPlayer? = null

    /**
     * Play scan success sound
     */
    fun playSuccessSound(context: Context) {
        try {
            // System notification sound
            val notification = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION)

            mediaPlayer?.release()
            mediaPlayer = MediaPlayer.create(context, notification)
            mediaPlayer?.setOnCompletionListener { mp ->
                mp.release()
            }
            mediaPlayer?.start()
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    /**
     * Release resources
     */
    fun release() {
        mediaPlayer?.release()
        mediaPlayer = null
    }
}