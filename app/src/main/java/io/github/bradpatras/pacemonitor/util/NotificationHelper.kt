package io.github.bradpatras.pacemonitor.util

import android.app.Notification
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import androidx.core.app.NotificationCompat
import io.github.bradpatras.pacemonitor.MainActivity
import io.github.bradpatras.pacemonitor.R

class NotificationHelper {
    companion object {
        val CHANNEL_ID = "io.github.bradpatras.pacemonitor.util"

        fun getNotification(context: Context): Notification {
            val openAppIntent = Intent(context, MainActivity::class.java)
            val openAppPendingIntent = PendingIntent.getActivity(context, 0, openAppIntent, 0)
            val builder = NotificationCompat.Builder(context, CHANNEL_ID)
                .setContentTitle(context.getString(R.string.notification_title))
                .setContentIntent(openAppPendingIntent)
                .setContentText(context.getString(R.string.notification_message))
                .setSmallIcon(R.drawable.ic_notification)
                .setStyle(NotificationCompat.BigTextStyle().bigText(context.getString(R.string.notification_message)))

            return builder.build()
        }
    }
}