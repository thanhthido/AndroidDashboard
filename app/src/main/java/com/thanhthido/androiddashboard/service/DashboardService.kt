package com.thanhthido.androiddashboard.service

import android.content.Intent
import androidx.lifecycle.LifecycleService
import androidx.lifecycle.lifecycleScope
import com.google.gson.Gson
import com.thanhthido.androiddashboard.data.mqtt_response.SensorData
import com.thanhthido.androiddashboard.di.dispatchers.DispatcherProvider
import com.thanhthido.androiddashboard.events.MQTTEvents
import com.thanhthido.androiddashboard.events.MqttConnectionStatusEvent
import com.thanhthido.androiddashboard.mqtt.MqttHelper
import com.thanhthido.androiddashboard.mqtt.MqttHelper.MQTT_PASSWORD
import com.thanhthido.androiddashboard.mqtt.MqttHelper.MQTT_URL
import com.thanhthido.androiddashboard.mqtt.MqttHelper.MQTT_USERNAME
import com.thanhthido.androiddashboard.mqtt.MqttTopic
import com.thanhthido.androiddashboard.utils.NotificationUtils.initNotification
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch
import org.eclipse.paho.android.service.MqttAndroidClient
import org.eclipse.paho.client.mqttv3.IMqttDeliveryToken
import org.eclipse.paho.client.mqttv3.MqttCallback
import org.eclipse.paho.client.mqttv3.MqttConnectOptions
import org.eclipse.paho.client.mqttv3.MqttMessage
import org.greenrobot.eventbus.EventBus
import timber.log.Timber
import javax.inject.Inject
import kotlin.random.Random

@AndroidEntryPoint
class DashboardService : LifecycleService() {

    @Inject
    lateinit var dispatcherProvider: DispatcherProvider

    private val mqttClient: MqttAndroidClient by lazy {
        MqttAndroidClient(this, MQTT_URL, "${Random.nextInt()}123456hdasjkdh")
    }

    private val mqttOptions: MqttConnectOptions by lazy {
        val options = MqttConnectOptions()
        options.apply {
            isCleanSession = true
            userName = MQTT_USERNAME
            password = MQTT_PASSWORD.toCharArray()
            connectionTimeout = 30
            keepAliveInterval = 60
        }
        options
    }

    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        lifecycleScope.launch(dispatcherProvider.default) {
            MqttHelper.connect(mqttClient, mqttOptions).collect { mqttState ->
                EventBus.getDefault().post(MqttConnectionStatusEvent(mqttState))

            }
        }

        mqttClient.setCallback(object : MqttCallback {
            override fun messageArrived(topic: String, message: MqttMessage) {
                if (topic != MqttTopic.SENSOR_DATA.topicName) return
                val response = Gson().fromJson(message.toString(), SensorData::class.java)
                EventBus.getDefault().post(MQTTEvents(response))
            }

            override fun connectionLost(cause: Throwable?) {
                Timber.e("Rot connection")
                return
            }

            override fun deliveryComplete(token: IMqttDeliveryToken?) = Unit
        })

        val notification = initNotification(this, this)
        startForeground(1, notification)
        return START_NOT_STICKY
    }

    override fun onDestroy() {
        super.onDestroy()
        mqttClient.unregisterResources()
        mqttClient.close()
    }


}