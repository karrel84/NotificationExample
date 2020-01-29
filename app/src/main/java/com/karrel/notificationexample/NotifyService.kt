package com.karrel.notificationexample

import android.app.*
import android.content.Context
import android.content.Intent
import android.os.Build
import android.os.IBinder
import android.widget.RemoteViews
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat
import io.reactivex.Observable
import java.util.concurrent.TimeUnit

class NotifyService : Service() {

    companion object {
        private const val CHANNEL_ID = "CHANNEL_ID_123"
        private const val NOTIFICATION_ID = 2
    }

    override fun onBind(p0: Intent?): IBinder? = null

    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        println("NotifyService > onStartCommand")

//        showNotification()
        showNotification2()

        return super.onStartCommand(intent, flags, startId)
    }

    override fun onCreate() {
        super.onCreate()

        println("NotifyService > onCreate")
        createNotificationChannel()
    }

    private fun showNotification2(){
        val fullScreenIntent = Intent(this, FullscreenActivity::class.java)
        val fullScreenPendingIntent = PendingIntent.getActivity(this, 0,
            fullScreenIntent, PendingIntent.FLAG_UPDATE_CURRENT)

        val notificationBuilder =
            NotificationCompat.Builder(this, CHANNEL_ID)
                .setSmallIcon(R.drawable.ic_launcher_foreground)
                .setContentTitle("Incoming call")
                .setContentText("(919) 555-1234")
                .setPriority(NotificationCompat.PRIORITY_HIGH)
                .setCategory(NotificationCompat.CATEGORY_CALL)

                // Use a full-screen intent only for the highest-priority alerts where you
                // have an associated activity that you would like to launch after the user
                // interacts with the notification. Also, if your app targets Android 10
                // or higher, you need to request the USE_FULL_SCREEN_INTENT permission in
                // order for the platform to invoke this notification.
                .setFullScreenIntent(fullScreenPendingIntent, true)

        val incomingCallNotification = notificationBuilder.build()
        startForeground(NOTIFICATION_ID, incomingCallNotification)

    }

    private fun createNotificationChannel() {
        // Create the NotificationChannel, but only on API 26+ because
        // the NotificationChannel class is new and not in the supportㄷ library
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val name = getString(R.string.channel_name)
            val descriptionText = getString(R.string.channel_description)
            val importance = NotificationManager.IMPORTANCE_HIGH
            val channel = NotificationChannel(CHANNEL_ID, name, importance).apply {
                description = descriptionText
                lockscreenVisibility = Notification.VISIBILITY_PRIVATE
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
            .setFullScreenIntent(PendingIntent.getActivity(this, 0, getFullScreenIntent(), 0), true)
//            .setContent(RemoteViews(packageName, R.layout.notification_content_view))
            .setCustomHeadsUpContentView(RemoteViews(packageName, R.layout.notification_handsup_content))
            .setCustomBigContentView(RemoteViews(packageName, R.layout.notification_big_content))

        startForeground(NOTIFICATION_ID, builder.build())


    }


    private fun getFullScreenIntent(): Intent {
        val intent = Intent(this, FullscreenActivity::class.java)
        intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK
        return intent
    }

}