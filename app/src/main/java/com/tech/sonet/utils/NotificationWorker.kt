package com.tech.sonet.utils

import android.app.ActivityManager
import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.media.RingtoneManager
import android.os.Build
import android.util.Log
import android.widget.RemoteViews
import androidx.core.app.NotificationCompat
import androidx.work.CoroutineWorker
import androidx.work.Worker
import androidx.work.WorkerParameters
import com.tech.sonet.R
import com.tech.sonet.data.api.CustomApiHelper
import com.tech.sonet.data.local.CustomSharedPrefManager
import com.tech.sonet.ui.HomeActivity

class NotificationWorker(context: Context, workerParams: WorkerParameters) : CoroutineWorker(context, workerParams) {

    private val sharedPrefManager = CustomSharedPrefManager(context)
   private val apiHelper = CustomApiHelper(context, sharedPrefManager)

    override suspend fun doWork(): Result {
        return try {
            // ✅ Step 1: Check if the app is in the background
            if (isAppInForeground(applicationContext)) {
                Log.d("NotificationWorker", "App is in foreground. Skipping notification.")
                return Result.success()
            }

            // ✅ Step 2: Check if location visibility is active
            val isLocationActive = sharedPrefManager.getLocation() ?: true
            Log.d("sadsasa", "doWork: $isLocationActive")
            if (isLocationActive) {
                sendNotification("Your location is currently visible to others. Would you like to switch Visibility OFF if you’re not actively using the app?")
            }

            // ✅ Step 3: Return Success
            Result.success()
        } catch (e: Exception) {
            Log.e("NotificationWorker", "Unexpected error", e)
            Result.retry() // Retry in case of an exception
        }
    }

    private fun sendNotification(message: String) {
        val intent = Intent(applicationContext, HomeActivity::class.java)
        val pendingIntent = PendingIntent.getActivity(
            applicationContext,
            0,
            intent,
            PendingIntent.FLAG_IMMUTABLE or PendingIntent.FLAG_UPDATE_CURRENT
        )

        val customView = RemoteViews(applicationContext.packageName, R.layout.custom_notification_layout)
        customView.setTextViewText(R.id.notification_title, "Sonet Notification")
        customView.setTextViewText(R.id.notification_message, message)

        val channelId = "local_channel_id"
        val notificationManager =
            applicationContext.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val channel = NotificationChannel(
                channelId,
                "Local Notifications",
                NotificationManager.IMPORTANCE_DEFAULT
            )
            notificationManager.createNotificationChannel(channel)
        }

        val notificationBuilder = NotificationCompat.Builder(applicationContext, channelId)
            .setAutoCancel(true)
            .setSmallIcon(R.drawable.sonet_icon)
            .setContentIntent(pendingIntent)
            .setCustomContentView(customView)
            .setStyle(NotificationCompat.DecoratedCustomViewStyle())

        notificationManager.notify(1, notificationBuilder.build())
    }

    // ✅ Helper function to check if the app is in the foreground
    private fun isAppInForeground(context: Context): Boolean {
        val activityManager = context.getSystemService(Context.ACTIVITY_SERVICE) as ActivityManager
        val runningProcesses = activityManager.runningAppProcesses ?: return false

        for (process in runningProcesses) {
            if (process.processName == context.packageName) {
                return process.importance == ActivityManager.RunningAppProcessInfo.IMPORTANCE_FOREGROUND
            }
        }
        return false
    }
}


