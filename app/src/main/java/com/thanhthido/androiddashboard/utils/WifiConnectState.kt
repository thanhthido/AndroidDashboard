package com.thanhthido.androiddashboard.utils

enum class WifiConnectStatus {
    CONNECTED,
    CONNECTING,
    ERROR
}

data class WifiConnectState<out T>(
    val wifiConnectStatus: WifiConnectStatus,
    val message: String?,
    val data: T?,
) {
    companion object {

        fun <T> loading(): WifiConnectState<T> {
            return WifiConnectState(WifiConnectStatus.CONNECTING, null, null)
        }

        fun <T> error(msg: String?): WifiConnectState<T> {
            return WifiConnectState(WifiConnectStatus.ERROR, msg, null)
        }

        fun <T> connected(data: T?): WifiConnectState<T> {
            return WifiConnectState(WifiConnectStatus.CONNECTED, null, data)
        }

    }
}