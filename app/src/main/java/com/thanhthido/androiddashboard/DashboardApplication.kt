package com.thanhthido.androiddashboard

import android.app.Application
import android.util.Log
import com.thanhthido.androiddashboard.utils.NotificationUtils.createNotificationChannel
import dagger.hilt.android.HiltAndroidApp
import timber.log.Timber

@HiltAndroidApp
class DashboardApplication : Application() {

    override fun onCreate() {
        super.onCreate()
        Timber.plant(Timber.DebugTree())

        createNotificationChannel(applicationContext)
    }

}