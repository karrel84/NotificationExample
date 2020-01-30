package com.karrel.notificationexample

import android.app.*
import android.content.Context
import android.content.Intent
import android.os.Build
import android.os.IBinder
import android.widget.RemoteViews
import androidx.core.app.NotificationCompat

class NotifyService : Service(), Mediable {

    companion object {
        private const val CHANNEL_ID = "CHANNEL_ID_3"
        private const val NOTIFICATION_ID = 2
    }

    private val vibrationArray: LongArray by lazy {
        longArrayOf( // 최대 60초
            1000L, 1000L, 1000L, 1000L, 1000L, 1000L, 1000L, 1000L, 1000L, 1000L
            , 1000L, 1000L, 1000L, 1000L, 1000L, 1000L, 1000L, 1000L, 1000L, 1000L
            , 1000L, 1000L, 1000L, 1000L, 1000L, 1000L, 1000L, 1000L, 1000L, 1000L
            , 1000L, 1000L, 1000L, 1000L, 1000L, 1000L, 1000L, 1000L, 1000L, 1000L
            , 1000L, 1000L, 1000L, 1000L, 1000L, 1000L, 1000L, 1000L, 1000L, 1000L
            , 1000L, 1000L, 1000L, 1000L, 1000L, 1000L, 1000L, 1000L, 1000L, 1000L
        )
    }

    private val ringtonePlayer: RingtonePlayer by lazy {
        RingtonePlayer(this)
    }

    override fun onBind(p0: Intent?): IBinder? = null

    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        println("NotifyService > onStartCommand")

        showNotification()

        return super.onStartCommand(intent, flags, startId)
    }

    override fun onCreate() {
        super.onCreate()

        println("NotifyService > onCreate")
        createNotificationChannel()
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


        val builder = NotificationCompat.Builder(this, CHANNEL_ID)
            .setSmallIcon(R.drawable.ic_launcher_foreground)
            .setContentTitle("My notification")
            .setContentText("Much longer text that cannot fit one line...")
//            .setStyle(NotificationCompat.DecoratedCustomViewStyle())
            .setStyle(NotificationCompat.BigTextStyle().bigText("Much longer text that cannot fit one line..."))
            .setPriority(NotificationCompat.PRIORITY_MAX)
            .setDefaults(Notification.DEFAULT_VIBRATE)
            .setCategory(NotificationCompat.CATEGORY_CALL)
            .setAutoCancel(false)
            .setVibrate(vibrationArray)
            .setFullScreenIntent(PendingIntent.getActivity(this, 0, getFullScreenIntent(), 0), true)
//            .setContent(RemoteViews(packageName, R.layout.notification_content_view))
            .setCustomHeadsUpContentView(
                RemoteViews(
                    packageName,
                    R.layout.notification_handsup_content
                )
            )
            .setCustomBigContentView(RemoteViews(packageName, R.layout.notification_big_content))

        startForeground(NOTIFICATION_ID, builder.build())

        playRingtone()
    }

    override fun playRingtone() {
        ringtonePlayer.playRingtone()
    }

    override fun stopRingtone() {
        ringtonePlayer.stopRingtone()
    }

    private fun getFullScreenIntent(): Intent {
        val intent = Intent(this, FullscreenActivity::class.java)
        intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK
        return intent
    }

}