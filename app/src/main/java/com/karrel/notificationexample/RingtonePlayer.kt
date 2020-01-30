package com.karrel.notificationexample

import android.content.Context
import android.media.MediaPlayer
import android.media.RingtoneManager
import android.net.Uri

class RingtonePlayer(context: Context) : Mediable {
    private val mediaPlayer: MediaPlayer by lazy {
        MediaPlayer.create(context, getRingtoneUri())
    }

    override fun playRingtone() {
        mediaPlayer.start()
    }

    override fun stopRingtone() {
        mediaPlayer.stop()
    }


    private fun getRingtoneUri(): Uri? {
        return RingtoneManager.getDefaultUri(RingtoneManager.TYPE_RINGTONE)
    }
}