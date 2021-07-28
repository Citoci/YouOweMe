package com.cito.youoweme.notifications

import android.app.Notification
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.util.Log
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat
import com.cito.youoweme.R
import com.cito.youoweme.notifications.utils.NotificationsUtils

class RememberNotificationBroadcastReceiver : BroadcastReceiver() {

    companion object {
        const val NOTIFICATION_ID_EXTRA = "com.cito.youoweme.notifications.RememberNotificationBroadcastReceiver.NOTIFICATION_ID_EXTRA"
        const val NOTIFICATION_TITLE_EXTRA = "com.cito.youoweme.notifications.RememberNotificationBroadcastReceiver.NOTIFICATION_TITLE_EXTRA_KEY"
        const val NOTIFICATION_TEXT_EXTRA = "com.cito.youoweme.notifications.RememberNotificationBroadcastReceiver.NOTIFICATION_TEXT_EXTRA_KEY"
    }

    override fun onReceive(context: Context?, intent: Intent?) {
        val notificationManager =  NotificationManagerCompat.from(context!!)

//        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
//            notificationManager.createNotificationChannel(
//                NotificationChannel(
//                    NotificationsUtils.REMEMBER_CHANNEL_ID,
//                    context.getString(R.string.notification_remember_channel_name),
//                    NotificationManager.IMPORTANCE_DEFAULT
//                )
//            )
//        }

        val notification: Notification =
            NotificationCompat.Builder(context, NotificationsUtils.REMEMBER_CHANNEL_ID)
                .setSmallIcon(R.drawable.ic_notifications_black_24dp)
                .setContentTitle(intent?.extras?.getString(NOTIFICATION_TITLE_EXTRA))
                .setContentText(intent?.extras?.getString(NOTIFICATION_TEXT_EXTRA))
                .build()

        notificationManager.notify(intent?.extras?.getInt(NOTIFICATION_ID_EXTRA)?:0, notification)
    }
}