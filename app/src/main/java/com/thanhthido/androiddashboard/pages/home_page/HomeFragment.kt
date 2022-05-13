package com.thanhthido.androiddashboard.pages.home_page

import android.app.ProgressDialog
import android.os.Bundle
import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.Toast
import com.thanhthido.androiddashboard.base.BaseFragment
import com.thanhthido.androiddashboard.databinding.FragmentHomeBinding
import com.thanhthido.androiddashboard.events.MQTTEvents
import com.thanhthido.androiddashboard.events.MqttConnectionStatusEvent
import com.thanhthido.androiddashboard.utils.MqttConnectStatus.*
import org.greenrobot.eventbus.EventBus
import org.greenrobot.eventbus.Subscribe
import org.greenrobot.eventbus.ThreadMode

@Suppress("DEPRECATION", "UNUSED", "SetTextI18n")
class HomeFragment : BaseFragment<FragmentHomeBinding>() {

    private val progressDialog by lazy {
        val progressDialog = ProgressDialog(requireContext())
        progressDialog.setMessage("Loading")
        progressDialog
    }

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
            binding.tvCoValue.text = "$value"
        }

        if (type.equals("no2", ignoreCase = true)) {
            binding.tvNo2Value.text = "$value"
        }

        if (type.equals("ch4", ignoreCase = true)) {
            binding.tvCh4Value.text = "$value"
        }

        if (type.equals("pm1", ignoreCase = true)) {
            binding.tvPm1Value.text = "$value"
        }

        if (type.equals("pm25", ignoreCase = true)) {
            binding.tvPm25Value.text = "$value"
        }

        if (type.equals("pm10", ignoreCase = true)) {
            binding.tvPm10Value.text = "$value"
        }

    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    fun receiveMqttConnectionState(mqttConnectionStatusEvent: MqttConnectionStatusEvent) {
        when (mqttConnectionStatusEvent.status.mqttConnectStatus) {
            CONNECTING -> {
                progressDialog.show()
            }
            CONNECTED -> {
                progressDialog.dismiss()
                Toast.makeText(
                    requireContext(),
                    "Kết nối với mqtt broker thành công",
                    Toast.LENGTH_SHORT
                ).show()
            }
            ERROR -> {
                progressDialog.dismiss()
                val msg = mqttConnectionStatusEvent.status.message
                msg?.let { message ->
                    if (message.isEmpty()) return@let
                    Toast.makeText(
                        requireContext(),
                        message,
                        Toast.LENGTH_SHORT
                    ).show()
                }

            }
        }
    }

    override fun initControls(savedInstanceState: Bundle?) = Unit

    override fun initEvents() = Unit

}