package com.thanhthido.androiddashboard.service

import android.app.ActivityManager
import android.app.ActivityManager.RunningAppProcessInfo.IMPORTANCE_FOREGROUND
import android.app.ActivityManager.RunningAppProcessInfo.IMPORTANCE_VISIBLE
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent

class BroadcastHandler : BroadcastReceiver() {

    companion object {
        const val REQUEST_CANCEL_SERVICE = "REQUEST_CANCEL_SERVICE"
    }

    override fun onReceive(context: Context, intent: Intent) {
        val cancelAction = intent.hasExtra(REQUEST_CANCEL_SERVICE)

        if (!cancelAction) return
        if (isAppInForeground()) return
        val service = Intent(context, DashboardService::class.java)
        context.stopService(service)
    }

    private fun isAppInForeground(): Boolean {
        val appProcessInfo = ActivityManager.RunningAppProcessInfo()
        ActivityManager.getMyMemoryState(appProcessInfo)
        return appProcessInfo.importance == IMPORTANCE_FOREGROUND || appProcessInfo.importance == IMPORTANCE_VISIBLE
    }
}