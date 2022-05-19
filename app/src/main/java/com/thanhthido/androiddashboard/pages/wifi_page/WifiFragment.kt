package com.thanhthido.androiddashboard.pages.wifi_page

import android.app.Activity.RESULT_OK
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.location.LocationManager
import android.net.wifi.WifiManager
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.android.gms.location.LocationRequest
import com.thanhthido.androiddashboard.adapters.WifiAdapter
import com.thanhthido.androiddashboard.base.BaseFragment
import com.thanhthido.androiddashboard.databinding.FragmentWifiBinding
import com.thanhthido.androiddashboard.events.WifiAdapterClick
import org.greenrobot.eventbus.EventBus
import org.greenrobot.eventbus.Subscribe
import org.greenrobot.eventbus.ThreadMode

class WifiFragment : BaseFragment<FragmentWifiBinding>() {

    private val wifiManager by lazy {
        requireActivity().applicationContext.getSystemService(Context.WIFI_SERVICE) as WifiManager
    }

    private val locationManager by lazy {
        requireActivity().getSystemService(Context.LOCATION_SERVICE) as LocationManager
    }

    // khoi tao de dang ki lang nghe wifi scanner
    private val wifiIntentFilter by lazy {
        IntentFilter().also {
            it.addAction(WifiManager.SCAN_RESULTS_AVAILABLE_ACTION)
        }
    }

    private val locationIntentFilter by lazy {
        IntentFilter().also {
            it.addAction(LocationManager.PROVIDERS_CHANGED_ACTION)
        }
    }

    private val wifiAdapter: WifiAdapter by lazy {
        WifiAdapter()
    }

    private val wifiListener = object : BroadcastReceiver() {
        override fun onReceive(context: Context, intent: Intent) {
            val isScanAvailable = intent.getBooleanExtra(WifiManager.EXTRA_RESULTS_UPDATED, false)
            if (isScanAvailable) {
                binding.containerWifiConnecting.visibility = View.GONE
                binding.rvListWifi.visibility = View.VISIBLE
                val listOfWifiVisible = wifiManager.scanResults.filter {
                    it.SSID.isNotEmpty()
                }
                wifiAdapter.setData(listOfWifiVisible.distinct())
            } else {
                binding.containerWifiConnecting.visibility = View.VISIBLE
                binding.tvMsgWifi.text = "Không có mạng Wi-Fi khả dụng"
                binding.rvListWifi.visibility = View.GONE
            }
        }
    }

    private val locationListener = object : BroadcastReceiver() {
        override fun onReceive(context: Context, intent: Intent) {
            intent.action?.let { action ->
                if (!action.matches(LocationManager.PROVIDERS_CHANGED_ACTION.toRegex())) return
                val isGpsEnable = locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER)
                if (isGpsEnable) {
                    hideErrorContainer()
                } else {
                    showErrorContainer()
                }
            }
        }
    }

    private val locationRequest by lazy {
        LocationRequest.create().also {
            it.priority = LocationRequest.PRIORITY_HIGH_ACCURACY
            it.interval = 5000
            it.fastestInterval = 2000
        }
    }

    private val checkLocationResult =
        registerForActivityResult(ActivityResultContracts.StartIntentSenderForResult()) { result ->
            if (result.resultCode == RESULT_OK) {
                hideErrorContainer()
            } else {
                showErrorContainer()
            }
        }

    override fun inflateViewBinding(
        inflater: LayoutInflater,
        container: ViewGroup?
    ) = FragmentWifiBinding.inflate(inflater, container, false)

    override fun initControls(savedInstanceState: Bundle?) {
        initRecyclerView()
        if (isLocationEnable(locationManager)) {
            hideErrorContainer()
            return
        }
        showErrorContainer()
    }

    override fun initEvents() {
        binding.switchScan.setOnCheckedChangeListener { _, checked ->
            if (checked) {
                binding.containerWifiConnecting.visibility = View.VISIBLE
                binding.rvListWifi.visibility = View.GONE
                binding.tvMsgWifi.text = "Không có mạng Wi-Fi khả dụng"
                wifiManager.startScan()
            } else {
                binding.containerWifiConnecting.visibility = View.VISIBLE
                binding.rvListWifi.visibility = View.GONE
                binding.tvMsgWifi.text = "Wi-Fi tắt"
            }
        }

        binding.tvContinueLocation.setOnClickListener {
            enableLocation(wifiManager, locationRequest, checkLocationResult, locationManager)
        }
    }

    override fun onStart() {
        super.onStart()
        if (!EventBus.getDefault().isRegistered(this)) {
            EventBus.getDefault().register(this)
        }
        requireContext().apply {
            registerReceiver(wifiListener, wifiIntentFilter)
            registerReceiver(locationListener, locationIntentFilter)
        }
    }

    override fun onStop() {
        super.onStop()
        EventBus.getDefault().unregister(this)

        requireContext().apply {
            unregisterReceiver(wifiListener)
            unregisterReceiver(locationListener)
        }
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    fun onWifiItemClick(wifiAdapterClick: WifiAdapterClick) {
        Toast.makeText(
            requireContext(),
            "${wifiAdapterClick.wifi.SSID} - isWifi5g: ${wifiAdapterClick.is5g}",
            Toast.LENGTH_SHORT
        ).show()
    }

    private fun initRecyclerView() {
        binding.rvListWifi.apply {
            layoutManager =
                LinearLayoutManager(requireContext(), LinearLayoutManager.VERTICAL, false)
            setHasFixedSize(true)
            setItemViewCacheSize(20)
            adapter = wifiAdapter
        }
    }

}