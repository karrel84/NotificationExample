package com.karrel.notificationexample

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import io.reactivex.Observable
import kotlinx.android.synthetic.main.activity_main.*
import java.util.concurrent.TimeUnit


class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        notification.setOnClickListener {
            startNotifyService()
        }
        delayNotification.setOnClickListener {
            Observable.timer(5, TimeUnit.SECONDS).subscribe {
                startNotifyService()
            }
        }
    }


    private fun startNotifyService() {
        val intent = Intent(this, NotifyService::class.java)
        startService(intent)
    }
}

