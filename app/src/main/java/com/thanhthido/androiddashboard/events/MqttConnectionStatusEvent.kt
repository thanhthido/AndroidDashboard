package com.thanhthido.androiddashboard.events

import com.thanhthido.androiddashboard.utils.MqttConnectState

data class MqttConnectionStatusEvent(
    val status: MqttConnectState<Unit>
)
