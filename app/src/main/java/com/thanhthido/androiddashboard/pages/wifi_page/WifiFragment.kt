package com.thanhthido.androiddashboard.pages.wifi_page

import android.app.Activity.RESULT_OK
import android.app.Dialog
import android.content.*
import android.location.LocationManager
import android.net.wifi.WifiManager
import android.os.Bundle
import android.text.TextUtils
import android.view.LayoutInflater
import android.view.ViewGroup
import android.view.Window
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import androidx.activity.result.contract.ActivityResultContracts
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.android.gms.location.LocationRequest
import com.thanhthido.androiddashboard.R
import com.thanhthido.androiddashboard.adapters.WifiAdapter
import com.thanhthido.androiddashboard.base.BaseFragment
import com.thanhthido.androiddashboard.data.arguments.WifiConnect
import com.thanhthido.androiddashboard.databinding.FragmentWifiBinding
import com.thanhthido.androiddashboard.events.WifiAdapterClick
import com.thanhthido.androiddashboard.utils.afterTextChanged
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

    private val wifiScanListener = object : BroadcastReceiver() {
        override fun onReceive(context: Context, intent: Intent) {
            val isScanAvailable = intent.getBooleanExtra(WifiManager.EXTRA_RESULTS_UPDATED, false)
            if (isScanAvailable && binding.switchScan.isChecked) {
                hideEmptyWifiScan()
                val listOfWifiVisible = wifiManager.scanResults.filter {
                    it.SSID.isNotEmpty()
                }
                wifiAdapter.setData(listOfWifiVisible.distinct())
                return
            }
            showEmptyWifiScan("Không có mạng Wi-Fi khả dụng")
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

    private val error5GHzDialog by lazy {
        Dialog(requireContext()).also { dialog ->
            dialog.apply {
                requestWindowFeature(Window.FEATURE_NO_TITLE)
                setCanceledOnTouchOutside(true)
                setContentView(R.layout.dialog_error_5ghz)
                window?.setLayout(
                    ViewGroup.LayoutParams.MATCH_PARENT,
                    ViewGroup.LayoutParams.WRAP_CONTENT
                )
            }
        }
    }

    private val buttonCancelDialog by lazy {
        error5GHzDialog.findViewById<Button>(R.id.btn_cancel_popup_5ghz)
    }

    private val dialogConnectWifi by lazy {
        Dialog(requireContext()).also { dialog ->
            dialog.apply {
                requestWindowFeature(Window.FEATURE_NO_TITLE)
                setCanceledOnTouchOutside(true)
                setContentView(R.layout.dialog_2ghz)
                window?.setLayout(
                    ViewGroup.LayoutParams.MATCH_PARENT,
                    ViewGroup.LayoutParams.WRAP_CONTENT
                )
            }
        }
    }

    private val ssidTitleDialog by lazy {
        dialogConnectWifi.findViewById<TextView>(R.id.tv_wifi_ssid)
    }


    private val edtPasswordWifi by lazy {
        dialogConnectWifi.findViewById<EditText>(R.id.edt_pass_wifi)
    }

    private val buttonConnectWifi by lazy {
        dialogConnectWifi.findViewById<Button>(R.id.btn_connect_wifi)
    }

    private var wifiSSID = ""
    private var wifiBSSID = ""

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
                showEmptyWifiScan("Không có mạng Wi-Fi khả dụng")
                wifiManager.startScan()
                return@setOnCheckedChangeListener
            }
            showEmptyWifiScan("Wi-Fi tắt")
        }

        binding.tvContinueLocation.setOnClickListener {
            enableLocation(wifiManager, locationRequest, checkLocationResult, locationManager)
        }

        buttonCancelDialog.setOnClickListener {
            error5GHzDialog.dismiss()
        }

        edtPasswordWifi.afterTextChanged { pass ->
            buttonConnectWifi.isEnabled = pass.length >= 8
        }

        buttonConnectWifi.setOnClickListener {
            val password = edtPasswordWifi.text.toString()
            if (TextUtils.isEmpty(password)) {
                return@setOnClickListener
            }

            dialogConnectWifi.dismiss()
            val action = WifiFragmentDirections.actionWifiToWifiConnect(
                WifiConnect(this.wifiSSID, this.wifiBSSID, password)
            )
            findNavController().navigate(action)
        }

        dialogConnectWifi.setOnDismissListener { edtPasswordWifi.setText("") }
    }

    override fun onStart() {
        super.onStart()
        if (!EventBus.getDefault().isRegistered(this)) {
            EventBus.getDefault().register(this)
        }
        requireContext().apply {
            registerReceiver(wifiScanListener, wifiIntentFilter)
            registerReceiver(locationListener, locationIntentFilter)
        }
    }

    override fun onStop() {
        super.onStop()
        EventBus.getDefault().unregister(this)

        requireContext().apply {
            unregisterReceiver(wifiScanListener)
            unregisterReceiver(locationListener)
        }
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    fun onWifiItemClick(wifiAdapterClick: WifiAdapterClick) {
        val (wifi, is5g) = wifiAdapterClick
        if (is5g) {
            error5GHzDialog.show()
            return
        }

        this.wifiSSID = wifi.SSID
        this.wifiBSSID = wifi.BSSID
        ssidTitleDialog.text = wifi.SSID
        dialogConnectWifi.show()
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