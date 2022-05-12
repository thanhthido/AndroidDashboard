package com.thanhthido.androiddashboard.pages.home_page

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import com.thanhthido.androiddashboard.R
import com.thanhthido.androiddashboard.base.BaseFragment
import com.thanhthido.androiddashboard.databinding.FragmentHomeBinding
import com.thanhthido.androiddashboard.events.MQTTEvents
import com.thanhthido.androiddashboard.events.MqttConnectionStatusEvent
import com.thanhthido.androiddashboard.utils.MqttConnectStatus
import org.greenrobot.eventbus.EventBus
import org.greenrobot.eventbus.Subscribe
import org.greenrobot.eventbus.ThreadMode

class HomeFragment : BaseFragment<FragmentHomeBinding>() {

    override fun inflateViewBinding(
        inflater: LayoutInflater,
        container: ViewGroup?
    ) = FragmentHomeBinding.inflate(inflater, container, false)

    override fun onStart() {
        super.onStart()
        EventBus.getDefault().register(this)
    }

    override fun onStop() {
        super.onStop()
        EventBus.getDefault().unregister(this)
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    fun receiveMqttSensorData(mqttEvents: MQTTEvents) {
        val (_, type, value) = mqttEvents.data

        if (type.equals("temperature", ignoreCase = true)) {
            binding.tvTempValue.text = "$value ℃"
        }

        if (type.equals("co", ignoreCase = true)) {
            binding.tvCoValue.text = "$value µg/m${requireContext().getString(R.string.unit_sup)}"
        }

        if (type.equals("no2", ignoreCase = true)) {
            binding.tvNo2Value.text = "$value µg/m${requireContext().getString(R.string.unit_sup)}"
        }

        if (type.equals("ch4", ignoreCase = true)) {
            binding.tvCh4Value.text = "$value µg/m${requireContext().getString(R.string.unit_sup)}"
        }

        if (type.equals("pm1", ignoreCase = true)) {
            binding.tvPm1Value.text = "$value µg/m${requireContext().getString(R.string.unit_sup)}"
        }

        if (type.equals("pm25", ignoreCase = true)) {
            binding.tvPm25Value.text = "$value µg/m${requireContext().getString(R.string.unit_sup)}"
        }

        if (type.equals("pm10", ignoreCase = true)) {
            binding.tvPm10Value.text = "$value µg/m${requireContext().getString(R.string.unit_sup)}"
        }

    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    fun receiveMqttConnectionState(mqttConnectionStatusEvent: MqttConnectionStatusEvent) {
        when (mqttConnectionStatusEvent.status.mqttConnectStatus) {
            MqttConnectStatus.CONNECTING -> {
                Log.d("MqttStatus", "loading")
            }
            MqttConnectStatus.CONNECTED -> {
                Log.d("MqttStatus", "connected")
            }
            MqttConnectStatus.ERROR -> {
                Log.d("MqttStatus", "error")
            }
        }
    }

    override fun initControls(savedInstanceState: Bundle?) = Unit

    override fun initEvents() = Unit

}