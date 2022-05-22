package com.thanhthido.androiddashboard.data.arguments

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class WifiConnect(
    val wifiSSID: String,
    val wifiBSSID: String,
    val wifiPassword: String
) : Parcelable
