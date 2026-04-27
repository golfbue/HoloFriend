package com.example.myapplication

import android.app.NotificationChannel
import android.app.NotificationManager
import android.content.Context
import android.os.Build
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat
import androidx.work.CoroutineWorker
import androidx.work.WorkerParameters
import android.util.Log

class LiveNotificationWorker(
    appContext: Context,
    workerParams: WorkerParameters
) : CoroutineWorker(appContext, workerParams) {

    private val api = HolodexApi.create(BuildConfig.HOLODEX_API_KEY)
    private val sharedPrefs = appContext.getSharedPreferences("holo_fan_prefs", Context.MODE_PRIVATE)

    override suspend fun doWork(): Result {
        return try {
            val liveStreams = api.getLiveStreams()
            
            // Get already notified stream IDs
            val notifiedIds = sharedPrefs.getStringSet("notified_live_ids", emptySet()) ?: emptySet()
            val newNotifiedIds = mutableSetOf<String>()
            
            for (stream in liveStreams) {
                newNotifiedIds.add(stream.id)
                
                if (stream.id !in notifiedIds) {
                    sendNotification(stream)
                }
            }
            
            // Save current live stream IDs to avoid duplicate notifications next time
            sharedPrefs.edit().putStringSet("notified_live_ids", newNotifiedIds).apply()
            
            Result.success()
        } catch (e: Exception) {
            Log.e("LiveWorker", "Error fetching live streams", e)
            Result.retry()
        }
    }

    private fun sendNotification(video: HolodexVideo) {
        val channelId = "live_notifications"
        val notificationId = video.id.hashCode()

        val builder = NotificationCompat.Builder(applicationContext, channelId)
            .setSmallIcon(android.R.drawable.ic_btn_speak_now) // Use a better icon if available
            .setContentTitle("${video.channel.name} กำลังไลฟ์!")
            .setContentText(video.title)
            .setPriority(NotificationCompat.PRIORITY_HIGH)
            .setAutoCancel(true)

        // Save to history
        val history = sharedPrefs.getStringSet("notification_history", emptySet())?.toMutableSet() ?: mutableSetOf()
        val timestamp = System.currentTimeMillis()
        val historyEntry = "${video.id}|${video.channel.name}|${video.title}|$timestamp"
        history.add(historyEntry)
        
        // Keep only latest 20
        val sortedHistory = history.sortedByDescending { it.split("|").last().toLongOrNull() ?: 0L }
        val limitedHistory = sortedHistory.take(20).toSet()
        
        sharedPrefs.edit().putStringSet("notification_history", limitedHistory).apply()

        with(NotificationManagerCompat.from(applicationContext)) {
            try {
                notify(notificationId, builder.build())
            } catch (e: SecurityException) {
                Log.e("LiveWorker", "Permission denied for notification", e)
            }
        }
    }

    companion object {
        const val CHANNEL_ID = "live_notifications"
        const val CHANNEL_NAME = "Live Notifications"
        
        fun createNotificationChannel(context: Context) {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                val importance = NotificationManager.IMPORTANCE_HIGH
                val channel = NotificationChannel(CHANNEL_ID, CHANNEL_NAME, importance).apply {
                    description = "แจ้งเตือนเมื่อมีเมมเบอร์เริ่มไลฟ์"
                }
                val notificationManager: NotificationManager =
                    context.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
                notificationManager.createNotificationChannel(channel)
            }
        }
    }
}
