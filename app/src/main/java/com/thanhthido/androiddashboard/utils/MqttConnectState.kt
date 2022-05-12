package com.thanhthido.androiddashboard.utils

enum class MqttConnectStatus {
    CONNECTED,
    CONNECTING,
    ERROR
}

data class MqttConnectState<out T>(
    val mqttConnectStatus: MqttConnectStatus,
    val message: String?
) {
    companion object {

        fun <T> loading(): MqttConnectState<T> {
            return MqttConnectState(MqttConnectStatus.CONNECTING, null)
        }

        fun <T> error(msg: String?): MqttConnectState<T> {
            return MqttConnectState(MqttConnectStatus.ERROR, msg)
        }

        fun <T> connected(): MqttConnectState<T> {
            return MqttConnectState(MqttConnectStatus.CONNECTED, null)
        }

    }
}