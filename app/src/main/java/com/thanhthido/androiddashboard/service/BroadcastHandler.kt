package com.thanhthido.androiddashboard.service

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent

class BroadcastHandler : BroadcastReceiver() {

    companion object {
        const val REQUEST_CANCEL_SERVICE = "REQUEST_CANCEL_SERVICE"
    }

    override fun onReceive(context: Context, intent: Intent) {
        val cancelAction = intent.hasExtra(REQUEST_CANCEL_SERVICE)

        if (cancelAction) {
            val service = Intent(context, DashboardService::class.java)
            context.stopService(service)
        }
    }
}