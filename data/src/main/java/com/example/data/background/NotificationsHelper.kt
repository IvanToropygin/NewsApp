package com.example.data.background

import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Context
import androidx.core.app.NotificationCompat
import androidx.core.content.getSystemService
import com.example.data.R
import dagger.hilt.android.qualifiers.ApplicationContext
import javax.inject.Inject

class NotificationsHelper @Inject constructor(
    @param:ApplicationContext private val context: Context,
    private val pendingIntent: PendingIntent
) {
    private val notificationManager = context.getSystemService<NotificationManager>()

    init {
        createNotificationChannel()
    }

    private fun createNotificationChannel() {
        val channel = NotificationChannel(
            CHANNEL_ID,
            context.getString(R.string.new_articles),
            NotificationManager.IMPORTANCE_DEFAULT
        )
        notificationManager?.createNotificationChannel(channel)
    }

    fun showNewArticlesNotification(topics: List<String>) {

        val notification = NotificationCompat.Builder(context, CHANNEL_ID)
            .setSmallIcon(R.drawable.ic_breaking_news)
            .setContentTitle(context.getString(R.string.new_articles_notification_title))
            .setContentText(
                context.getString(
                    R.string.update_subscriptions_notification_text,
                    topics.size, topics.joinToString(", ")
                )
            )
            .setContentIntent(pendingIntent)
            .setAutoCancel(true)
            .build()
        notificationManager?.notify(
            NOTIFICATION_ID,
            notification
        )
    }

    companion object {
        private const val CHANNEL_ID = "new_articles"
        private const val NOTIFICATION_ID = 1
    }
}