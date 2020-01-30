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
        mediaPlayer.seekTo(0)
        mediaPlayer.start()
    }

    override fun stopRingtone() {
        mediaPlayer.pause()
    }


    private fun getRingtoneUri(): Uri? {
        return RingtoneManager.getDefaultUri(RingtoneManager.TYPE_RINGTONE)
    }
}