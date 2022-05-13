package com.thanhthido.androiddashboard.mqtt

import com.thanhthido.androiddashboard.utils.MqttConnectState
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import org.eclipse.paho.android.service.MqttAndroidClient
import org.eclipse.paho.client.mqttv3.MqttConnectOptions
import org.eclipse.paho.client.mqttv3.MqttException

object MqttHelper {

    private const val MQTT_PORT = 18675
    const val MQTT_URL = "tcp://driver.cloudmqtt.com:$MQTT_PORT"
    const val MQTT_USERNAME = "burlgbdf"
    const val MQTT_PASSWORD = "0--UiYtSUWAZ"

    suspend fun connect(
        mqttClient: MqttAndroidClient,
        options: MqttConnectOptions
    ): Flow<MqttConnectState<Unit>> =
        flow {
            emit(MqttConnectState.loading())
            try {
                if (mqttClient.isConnected) {
                    emit(MqttConnectState.error(""))
                    return@flow
                }
                mqttClient.apply {
                    connect(options, null, null).waitForCompletion()
                    subscribe(MqttTopic.SENSOR_DATA.topicName, 0, null, null).waitForCompletion()
                }
                emit(MqttConnectState.connected())
            } catch (e: MqttException) {
                e.printStackTrace()
                emit(MqttConnectState.error(e.message))
            }
        }
}

enum class MqttTopic(val topicName: String) {
    SENSOR_DATA(topicName = "nodeWiFi32/detail")
}

