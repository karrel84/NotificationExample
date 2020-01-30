package com.karrel.notificationexample

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity

/**
 * An example full-screen activity that shows and hides the system UI (i.e.
 * status bar and navigation/system bar) with user interaction.
 */
class FullscreenActivity : AppCompatActivity() {

    private val broadCastReceiver = object : BroadcastReceiver() {
        override fun onReceive(context: Context?, intent: Intent?) {
            println("FullscreenActivity > onReceive action : ${intent?.action}")

            val action = intent?.action

            when(action) {
                Intent.ACTION_SCREEN_ON -> cancelNotification()
            }
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContentView(R.layout.activity_fullscreen)

        println("FullscreenActivity > onCreate")


        registerReceiver()
    }

    private fun registerReceiver() {
        val filter = IntentFilter(Intent.ACTION_SCREEN_ON)
        filter.addAction(Intent.ACTION_SCREEN_OFF)
        registerReceiver(broadCastReceiver, filter)
    }

    override fun onStart() {
        super.onStart()

        println("FullscreenActivity > onStart")
    }

    override fun onResume() {
        super.onResume()

        println("FullscreenActivity > onResume")
    }

    override fun onDestroy() {
        super.onDestroy()
        unregisterReceiver(broadCastReceiver)
    }

    private fun cancelNotification() {
        val intent = Intent(this, NotifyService::class.java).apply {
            putExtra(NotifyService.REQUEST_CODE, NotifyService.REQUEST_CODE_CANCEL)
        }
        startService(intent)
    }

}
