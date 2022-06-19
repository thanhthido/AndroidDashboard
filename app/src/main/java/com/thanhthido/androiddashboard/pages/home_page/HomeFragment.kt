package com.thanhthido.androiddashboard.pages.home_page

import android.app.ProgressDialog
import android.os.Bundle
import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.viewModels
import com.thanhthido.androiddashboard.base.BaseFragment
import com.thanhthido.androiddashboard.databinding.FragmentHomeBinding
import com.thanhthido.androiddashboard.events.MQTTEvents
import com.thanhthido.androiddashboard.events.MqttConnectionStatusEvent
import com.thanhthido.androiddashboard.utils.MqttConnectStatus.*
import dagger.hilt.android.AndroidEntryPoint
import org.greenrobot.eventbus.EventBus
import org.greenrobot.eventbus.Subscribe
import org.greenrobot.eventbus.ThreadMode
import kotlin.math.roundToInt

@Suppress("DEPRECATION", "UNUSED", "SetTextI18n")
@AndroidEntryPoint
class HomeFragment : BaseFragment<FragmentHomeBinding>() {

    private val viewModel: HomeViewModel by viewModels()

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
            binding.tvTempValue.text = "$value ℃" // 1
        }

        if (type.equals("co", ignoreCase = true)) {
            binding.tvCoValue.text = "${value.roundToInt()}"
        }

        if (type.equals("no2", ignoreCase = true)) {
            binding.tvNo2Value.text = "$value" // 1
        }

        if (type.equals("ch4", ignoreCase = true)) {
            binding.tvCh4Value.text = "${value.roundToInt()}"
        }

        if (type.equals("pm1", ignoreCase = true)) {
            binding.tvPm1Value.text = "${value.roundToInt()}"
        }

        if (type.equals("pm25", ignoreCase = true)) {
            binding.tvPm25Value.text = "${value.roundToInt()}"
        }

        if (type.equals("pm10", ignoreCase = true)) {
            binding.tvPm10Value.text = "${value.roundToInt()}"
        }

    }

    override fun initControls(savedInstanceState: Bundle?) {
        subscribeLatestData(viewModel)
        viewModel.getLatestData()
    }

    override fun initEvents() {
        binding.swipeRefreshHome.setOnRefreshListener {
            binding.swipeRefreshHome.isRefreshing = true
            viewModel.getLatestData()
        }
    }

}