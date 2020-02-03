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

        setupButtonEvents()
    }

    private fun setupButtonEvents() {
        notification.setOnClickListener {
            startNotifyService()
        }
        delayNotification.setOnClickListener {
            Observable.timer(5, TimeUnit.SECONDS).subscribe {
                startNotifyService()
            }
        }

        intentTest.setOnClickListener {
            val intent = Intent(this, FullscreenActivity::class.java)
                .apply {
                    val bundle = Bundle().apply {
                        putParcelable(FullscreenActivity.EXTRA_NAME, TestParcel("hello"))
                    }
                    putExtras(bundle)
                }
            startActivity(intent)
        }
    }


    private fun startNotifyService() {
        val intent = Intent(this, NotifyService::class.java)
            .apply {
                val bundle = Bundle().apply {
                    putParcelable(FullscreenActivity.EXTRA_NAME, TestParcel("hello"))
                }
                putExtras(bundle)
            }
        startService(intent)
    }
}