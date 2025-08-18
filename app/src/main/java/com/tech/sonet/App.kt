package com.tech.sonet

import android.app.Activity
import android.app.Application
import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.lifecycle.ProcessLifecycleOwner
import androidx.work.ExistingPeriodicWorkPolicy
import androidx.work.ExistingWorkPolicy
import androidx.work.OneTimeWorkRequestBuilder
import androidx.work.PeriodicWorkRequestBuilder
import androidx.work.WorkManager
import com.tech.sonet.utils.AppLifecycleObserver
import com.tech.sonet.utils.NotificationWorker
import dagger.hilt.android.HiltAndroidApp
import java.util.concurrent.TimeUnit

@HiltAndroidApp
class App : Application() {
    private var isRestarting: Boolean = false

    override fun onCreate() {
        super.onCreate()
// Attach lifecycle observer
        // Register the lifecycle observer to monitor app background and foreground state
        val appLifecycleObserver = AppLifecycleObserver(this)
        ProcessLifecycleOwner.get().lifecycle.addObserver(appLifecycleObserver)
    }
    fun onLogout() {
        restartApp()
    }

    private fun restartApp() {
        if (!isRestarting) {
            isRestarting = true
            val intent = baseContext.packageManager.getLaunchIntentForPackage(baseContext.packageName)
            intent?.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK or Intent.FLAG_ACTIVITY_NEW_TASK)
            startActivity(intent)
        }
    }

}
