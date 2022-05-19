package com.thanhthido.androiddashboard.events

import android.net.wifi.ScanResult

data class WifiAdapterClick(
    val wifi: ScanResult,
    val is5g: Boolean
)
