package com.thanhthido.androiddashboard.service

import android.app.Service
import android.content.Intent
import android.os.IBinder
import com.google.gson.Gson
import com.thanhthido.androiddashboard.data.mqtt_response.SensorData
import com.thanhthido.androiddashboard.events.MQTTEvents
import com.thanhthido.androiddashboard.events.MqttConnectionStatusEvent
import com.thanhthido.androiddashboard.mqtt.MqttHelper
import com.thanhthido.androiddashboard.mqtt.MqttHelper.MQTT_PASSWORD
import com.thanhthido.androiddashboard.mqtt.MqttHelper.MQTT_URL
import com.thanhthido.androiddashboard.mqtt.MqttHelper.MQTT_USERNAME
import com.thanhthido.androiddashboard.mqtt.MqttTopic
import com.thanhthido.androiddashboard.utils.NotificationUtils.initNotification
import kotlinx.coroutines.*
import org.eclipse.paho.android.service.MqttAndroidClient
import org.eclipse.paho.client.mqttv3.IMqttDeliveryToken
import org.eclipse.paho.client.mqttv3.MqttCallback
import org.eclipse.paho.client.mqttv3.MqttConnectOptions
import org.eclipse.paho.client.mqttv3.MqttMessage
import org.greenrobot.eventbus.EventBus
import timber.log.Timber
import kotlin.random.Random

class DashboardService : Service() {


    private val job = SupervisorJob()
    private val scope = CoroutineScope(Dispatchers.Default + job)

    private val mqttClient: MqttAndroidClient by lazy {
        MqttAndroidClient(this, MQTT_URL, "${Random.nextInt()}123456")
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

        scope.launch {
            val connectStatus = async {
                MqttHelper.connect(mqttClient, mqttOptions)
            }
            withContext(Dispatchers.Main) {
                EventBus.getDefault().post(MqttConnectionStatusEvent(connectStatus.await()!!))
            }
        }

        mqttClient.setCallback(object : MqttCallback {
            override fun messageArrived(topic: String, message: MqttMessage) {
                if (topic != MqttTopic.Sensors().topic) return
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

    override fun onBind(p0: Intent?): IBinder? {
        return null
    }

    override fun onDestroy() {
        super.onDestroy()
        job.cancel()
    }


}