package com.tech.sonet.utils

import android.app.Application
import android.util.Log
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleObserver
import androidx.lifecycle.OnLifecycleEvent
import androidx.lifecycle.ProcessLifecycleOwner
import androidx.work.ExistingPeriodicWorkPolicy
import androidx.work.OneTimeWorkRequest
import androidx.work.OneTimeWorkRequestBuilder
import androidx.work.PeriodicWorkRequestBuilder
import androidx.work.WorkManager
import androidx.work.WorkerParameters
import com.tech.sonet.data.api.ApiHelper
import com.tech.sonet.data.api.CustomApiHelper
import com.tech.sonet.data.local.CustomSharedPrefManager
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import java.sql.Time
import java.util.concurrent.TimeUnit

class AppLifecycleObserver(private val context: Application) : LifecycleObserver {
    private val sharedPrefManager = CustomSharedPrefManager(context)
    private val apiHelper = CustomApiHelper(context, sharedPrefManager) // Initialized at declaration


    @OnLifecycleEvent(Lifecycle.Event.ON_STOP)
    fun onAppBackgrounded() {
        CoroutineScope(Dispatchers.IO).launch {
            val apiResult = apiHelper.apiBackGround(Constant.USER_ACTIVITY) // Pass "background" type
            Log.d("AppLifecycleObserver", "API call result (background): $apiResult") }

        scheduleNotificationWork(context)
    }

    @OnLifecycleEvent(Lifecycle.Event.ON_START)
    fun onAppForegrounded() {
        CoroutineScope(Dispatchers.IO).launch {
            val apiResult = apiHelper.apiForeGround(Constant.USER_ACTIVITY) // Pass "foreground" type
            Log.d("AppLifecycleObserver", "API call result (foreground): $apiResult")
        }

        WorkManager.getInstance(context).cancelUniqueWork("HourlyNotificationWork")
    }

    private fun scheduleNotificationWork(context: Application) {
        val workRequest = PeriodicWorkRequestBuilder<NotificationWorker>(1, TimeUnit.HOURS)
            .setInitialDelay(1, TimeUnit.HOURS)
            .build()

        WorkManager.getInstance(context).enqueueUniquePeriodicWork(
            "HourlyNotificationWork",
            ExistingPeriodicWorkPolicy.REPLACE,
            workRequest
        )
    }
}



