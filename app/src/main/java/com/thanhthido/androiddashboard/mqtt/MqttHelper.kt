package com.thanhthido.androiddashboard.mqtt

import com.thanhthido.androiddashboard.utils.MqttConnectState
import kotlinx.coroutines.*
import org.eclipse.paho.android.service.MqttAndroidClient
import org.eclipse.paho.client.mqttv3.IMqttActionListener
import org.eclipse.paho.client.mqttv3.IMqttToken
import org.eclipse.paho.client.mqttv3.MqttConnectOptions
import org.eclipse.paho.client.mqttv3.MqttException
import timber.log.Timber

object MqttHelper {

    private const val MQTT_PORT = 18675
    const val MQTT_URL = "tcp://driver.cloudmqtt.com:$MQTT_PORT"
    const val MQTT_USERNAME = "burlgbdf"
    const val MQTT_PASSWORD = "0--UiYtSUWAZ"

    suspend fun connect(mqttClient: MqttAndroidClient, options: MqttConnectOptions) =
        withContext(Dispatchers.Default) {
            MqttConnectState.loading<Unit>()
            try {
                var mqttConnectStatus: MqttConnectState<Unit>? = null
                val connecting = async {
                    mqttClient.connect(options, null, null)
                }

                val a = async {
                    connecting.await().actionCallback
                }

//                a.await().onSuccess()

                mqttConnectStatus
            } catch (e: MqttException) {
                e.printStackTrace()
                MqttConnectState.error(e.message)
            }
        }

    private suspend fun subscribe(
        mqttClient: MqttAndroidClient,
        mqttTopic: MqttTopic,
        qos: Int = 0
    ): Pair<Boolean, String>? {
        var isSubscribe: Pair<Boolean, String>? = null
        try {
            withContext(Dispatchers.Main) {
                val subscribing = async {
                    mqttClient.subscribe(mqttTopic.topic, qos, null, null)
                }
                subscribing.await().actionCallback = object : IMqttActionListener {
                    override fun onSuccess(asyncActionToken: IMqttToken?) {
                        Timber.d("Subscribe thanh cong")
                        isSubscribe = Pair(true, "")
                    }

                    override fun onFailure(asyncActionToken: IMqttToken?, exception: Throwable?) {
                        Timber.e("Subscribe that bai")
                        isSubscribe = Pair(false, exception?.message ?: "khong the subscibe")
                    }
                }
            }
        } catch (e: MqttException) {
            e.printStackTrace()
            isSubscribe = Pair(false, e.message ?: "khong the subscibe")
        }
        return isSubscribe
    }

}

sealed class MqttTopic(val topic: String) {
    class Sensors(topic: String = "nodeWiFi32/detail") : MqttTopic(topic)
}

