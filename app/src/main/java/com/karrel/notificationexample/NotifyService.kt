package com.karrel.notificationexample

import android.app.*
import android.content.Context
import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.os.IBinder
import android.widget.RemoteViews
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat


class NotifyService : Service(), Mediable {

    companion object {
        private const val CHANNEL_ID = "CHANNEL_ID_9"
        private const val NOTIFICATION_ID = 4

        const val REQUEST_CODE = "request-code"
        const val REQUEST_CODE_SHOW_NOTI = 1234
        const val REQUEST_CODE_CANCEL = 1111
        const val REQUEST_CODE_ACCEPT = 1112
        const val REQUEST_CODE_NOTI_CANCEL = 1113
    }

    private var notificationId = 1

    private val vibrationArray: LongArray by lazy {
        longArrayOf( // 최대 60초
        )
//        longArrayOf( // 최대 60초
//            1000L, 1000L, 1000L, 1000L, 1000L, 1000L, 1000L, 1000L, 1000L, 1000L
//            , 1000L, 1000L, 1000L, 1000L, 1000L, 1000L, 1000L, 1000L, 1000L, 1000L
//            , 1000L, 1000L, 1000L, 1000L, 1000L, 1000L, 1000L, 1000L, 1000L, 1000L
//            , 1000L, 1000L, 1000L, 1000L, 1000L, 1000L, 1000L, 1000L, 1000L, 1000L
//            , 1000L, 1000L, 1000L, 1000L, 1000L, 1000L, 1000L, 1000L, 1000L, 1000L
//            , 1000L, 1000L, 1000L, 1000L, 1000L, 1000L, 1000L, 1000L, 1000L, 1000L
//        )
    }

    private val ringtonePlayer: RingtonePlayer by lazy {
        RingtonePlayer(this)
    }

    override fun onBind(p0: Intent?): IBinder? = null

    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        println("NotifyService > onStartCommand > startId : $startId, flags : $flags")

        val requestCode = intent?.getIntExtra(REQUEST_CODE, -1)
        println("NotifyService > onStartCommand > requestCode : $requestCode")

        when (requestCode) {
            REQUEST_CODE_ACCEPT -> {
                stopForeground(true)
                onAccept()
            }
            REQUEST_CODE_CANCEL -> onCancel()
            REQUEST_CODE_NOTI_CANCEL -> stopForeground(true)
            REQUEST_CODE_SHOW_NOTI -> {
                NotificationManagerCompat.from(this).cancelAll()
                showNotification()
//                playRingtone()
            }
        }

        return START_REDELIVER_INTENT
    }

    override fun onCreate() {
        super.onCreate()

        println("NotifyService > onCreate")
        createNotificationChannel()
    }

    override fun onDestroy() {
        super.onDestroy()

        stopRingtone()
        stopForeground(true)
    }


    private fun onCancel() {
        stopRingtone()
        stopForeground(true)
    }

    private fun onAccept() {
        startActivity(getFullScreenIntent())
    }


    private fun createNotificationChannel() {
        // Create the NotificationChannel, but only on API 26+ because
        // the NotificationChannel class is new and not in the support library
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val name = getString(R.string.channel_name)
            val descriptionText = getString(R.string.channel_description)
            val importance = NotificationManager.IMPORTANCE_HIGH
            val channel = NotificationChannel(CHANNEL_ID, name, importance).apply {
                description = descriptionText
                lockscreenVisibility = Notification.VISIBILITY_PRIVATE
                vibrationPattern = vibrationArray
                enableVibration(true)
            }


            // Register the channel with the system
            val notificationManager: NotificationManager =
                getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
            notificationManager.createNotificationChannel(channel)
        }
    }

    private fun showNotification() {
        println("NotifyService > showNotification()")

        val headsupView = createRemoteView()

        val builder = NotificationCompat.Builder(this, CHANNEL_ID)
            .setSmallIcon(R.drawable.ic_launcher_foreground)
            .setContentTitle("Slide")
            .setContentText("Much longer text that cannot fit one line...")
            .setStyle(NotificationCompat.BigTextStyle().bigText("Much longer text that cannot fit one line..."))
            .setPriority(NotificationCompat.PRIORITY_MAX)
            .setDefaults(Notification.DEFAULT_VIBRATE)
            .setCategory(NotificationCompat.CATEGORY_CALL)
            .setAutoCancel(false)
            .setVibrate(vibrationArray)
            .setFullScreenIntent(PendingIntent.getActivity(this, 0, getFullScreenIntent(), 0), true)
            .setCustomHeadsUpContentView(headsupView)
            .setCustomBigContentView(headsupView)

        startForeground(notificationId, builder.build())


    }

    private fun createIntent(requestCode: Int): Intent {
        return Intent(this, NotifyService::class.java).apply {
            putExtra(REQUEST_CODE, requestCode)
        }
    }

    private fun createRemoteView(): RemoteViews {
        val remoteView = RemoteViews(packageName, R.layout.notification_handsup_content)
        remoteView.setTextViewText(R.id.name, "빅토르")


        remoteView.setOnClickPendingIntent(
            R.id.cancel,
            PendingIntent.getService(
                this,
                REQUEST_CODE_CANCEL,
                createIntent(REQUEST_CODE_CANCEL),
                PendingIntent.FLAG_UPDATE_CURRENT
            )
        )
        remoteView.setOnClickPendingIntent(
            R.id.accept,
            PendingIntent.getService(
                this,
                REQUEST_CODE_ACCEPT,
                createIntent(REQUEST_CODE_ACCEPT),
                PendingIntent.FLAG_UPDATE_CURRENT
            )
        )

        return remoteView
    }

    override fun playRingtone() {
        ringtonePlayer.playRingtone()
    }

    override fun stopRingtone() {
        ringtonePlayer.stopRingtone()
    }

    private fun getFullScreenIntent(): Intent {
        val intent = Intent(this, FullscreenActivity::class.java)
            .apply {
                putExtra(FullscreenActivity.EXTRA_NAME, TestParcel("hello"))
            }
        intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK
        return intent
    }

}