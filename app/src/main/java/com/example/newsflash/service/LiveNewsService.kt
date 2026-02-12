package com.example.newsflash.service

import android.app.*
import android.content.Intent
import android.os.Build
import android.os.IBinder
import android.util.Log
import androidx.core.app.NotificationCompat
import com.example.newsflash.R
import com.example.newsflash.network.fetchNews
import kotlinx.coroutines.*

class LiveNewsService : Service() {

    private val serviceScope = CoroutineScope(Dispatchers.IO + SupervisorJob())
    private var lastUrl: String? = null

    companion object {
        const val CHANNEL_ID = "live_news_channel"
        const val NOTIFICATION_ID = 1

        const val INTERVAL = 5 * 60 * 1000L // 5 minutes
//        const val INTERVAL = 15 * 60 * 1000L // 15 min

    }

    override fun onCreate() {
        super.onCreate()
        createNotificationChannel()
        startForeground(NOTIFICATION_ID, createBaseNotification())
        startNewsUpdates()
    }

    private fun startNewsUpdates() {
        serviceScope.launch {
            while (true) {

                val articles = fetchNews()
                val latest = articles.firstOrNull()

                latest?.let {
                    if (it.url != lastUrl) {
                        lastUrl = it.url
                        showUpdateNotification(it.title)
                    }
                }

                delay(INTERVAL)
            }
        }
    }

    private fun createBaseNotification(): Notification {

        val stopIntent = Intent(this, LiveNewsService::class.java).apply {
            action = "STOP_SERVICE"
        }

        val stopPendingIntent = PendingIntent.getService(
            this,
            0,
            stopIntent,
            PendingIntent.FLAG_IMMUTABLE
        )

        return NotificationCompat.Builder(this, CHANNEL_ID)
            .setContentTitle("Live News Active")
            .setContentText("Checking for latest updates...")
            .setSmallIcon(R.drawable.ic_notification)
            .setOngoing(true)
            .addAction(
                R.drawable.ic_notification,
                "Stop",
                stopPendingIntent
            )
            .build()
    }

    private fun showUpdateNotification(title: String?) {
        val stopIntent = Intent(this, LiveNewsService::class.java).apply {
            action = "STOP_SERVICE"
        }

        val stopPendingIntent = PendingIntent.getService(
            this,
            0,
            stopIntent,
            PendingIntent.FLAG_IMMUTABLE
        )
        val notification = NotificationCompat.Builder(this, CHANNEL_ID)
            .setContentTitle("📰 New Article")
            .setContentText(title)
            .setSmallIcon(R.drawable.ic_notification)
//            .setAutoCancel(true)
            .setOngoing(true)
            .addAction(
                R.drawable.ic_notification,
                "Stop",
                stopPendingIntent
            )
            .build()

        val manager = getSystemService(NotificationManager::class.java)
        manager.notify(System.currentTimeMillis().toInt(), notification)
    }

    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {

        if (intent?.action == "STOP_SERVICE") {
            stopSelf()
        }

        return START_STICKY
    }


    private fun createNotificationChannel() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val channel = NotificationChannel(
                CHANNEL_ID,
                "Live News",
                NotificationManager.IMPORTANCE_HIGH
            )
            getSystemService(NotificationManager::class.java)
                .createNotificationChannel(channel)
        }
    }

    override fun onDestroy() {
        serviceScope.cancel()
        super.onDestroy()
    }

    override fun onBind(intent: Intent?): IBinder? = null
}
