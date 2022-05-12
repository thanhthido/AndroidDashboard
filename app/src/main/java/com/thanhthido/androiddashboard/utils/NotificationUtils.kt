package com.thanhthido.androiddashboard.utils

import android.app.Notification
import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.os.Build
import android.renderscript.RenderScript
import androidx.core.app.NotificationCompat
import com.thanhthido.androiddashboard.R
import com.thanhthido.androiddashboard.pages.MainActivity
import com.thanhthido.androiddashboard.service.BroadcastHandler
import com.thanhthido.androiddashboard.service.BroadcastHandler.Companion.REQUEST_CANCEL_SERVICE
import com.thanhthido.androiddashboard.service.DashboardService

object NotificationUtils {

    private const val CHANNEL_ID = "com.thanhthido.androiddashboard.service"

    fun createNotificationChannel(context: Context) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val notificationChannel = NotificationChannel(
                CHANNEL_ID,
                "com.thanhthido.androiddashboard.service",
                NotificationManager.IMPORTANCE_DEFAULT
            )

            val manager =
                context.getSystemService(NotificationManager::class.java) as NotificationManager

            manager.createNotificationChannel(notificationChannel)
        }
    }

    fun initNotification(context: Context, dashboardService: DashboardService): Notification {
        val goBackToAppIntent = Intent(dashboardService, MainActivity::class.java).let { intent ->
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP)
            PendingIntent.getActivity(context, 0, intent, 0)
        }

        val cancelService = Intent(dashboardService, BroadcastHandler::class.java).let { intent ->
            intent.putExtra(REQUEST_CANCEL_SERVICE, "cancel_service")
            PendingIntent.getBroadcast(context, 0, intent, PendingIntent.FLAG_UPDATE_CURRENT)
        }

        return NotificationCompat.Builder(context, CHANNEL_ID)
            .setContentTitle("Dashboard App")
            .setContentText("App đang chạy")
            .setSmallIcon(R.drawable.ic_android)
            .addAction(R.drawable.ic_close, "Cancel", cancelService)
            .setContentIntent(goBackToAppIntent)
            .setPriority(NotificationCompat.PRIORITY_DEFAULT)
            .setAutoCancel(true)
            .build()
    }


}