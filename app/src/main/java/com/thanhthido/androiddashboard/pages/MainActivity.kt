package com.thanhthido.androiddashboard.pages

import android.app.ActivityManager
import android.app.ProgressDialog
import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.ui.setupWithNavController
import com.thanhthido.androiddashboard.R
import com.thanhthido.androiddashboard.databinding.ActivityMainBinding
import com.thanhthido.androiddashboard.events.MqttConnectionStatusEvent
import com.thanhthido.androiddashboard.service.DashboardService
import com.thanhthido.androiddashboard.utils.MqttConnectStatus
import dagger.hilt.android.AndroidEntryPoint
import org.greenrobot.eventbus.EventBus
import org.greenrobot.eventbus.Subscribe
import org.greenrobot.eventbus.ThreadMode


@Suppress("DEPRECATION")
@AndroidEntryPoint
class MainActivity : AppCompatActivity() {

    private val progressDialog by lazy {
        val progressDialog = ProgressDialog(this)
        progressDialog.setMessage("Loading")
        progressDialog
    }

    private lateinit var binding: ActivityMainBinding
    private val dashboardService by lazy {
        Intent(this, DashboardService::class.java)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setUpBottomNavigation()
        startDashboardService()
    }

    override fun onStart() {
        super.onStart()
        EventBus.getDefault().register(this)
    }

    override fun onStop() {
        super.onStop()
        EventBus.getDefault().unregister(this)
    }

    private fun startDashboardService() {
        val isDashboardServiceRunning = isServiceRunning(".service.DashboardService")
        if (isDashboardServiceRunning) return
        ContextCompat.startForegroundService(this, dashboardService)
    }

    private fun isServiceRunning(serviceClassName: String): Boolean {
        val activityManager =
            ContextCompat.getSystemService(this, ActivityManager::class.java) ?: return false

        return activityManager.getRunningServices(Int.MAX_VALUE).any { serviceInfo ->
            serviceInfo.service.shortClassName == serviceClassName
        }
    }

    private fun setUpBottomNavigation() {
        val navHostFragment =
            supportFragmentManager.findFragmentById(R.id.nav_host_fragment) as NavHostFragment
        val navController = navHostFragment.navController

        binding.bottomNavigationView.setupWithNavController(navController)
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    fun receiveMqttConnectionState(mqttConnectionStatusEvent: MqttConnectionStatusEvent) {
        when (mqttConnectionStatusEvent.status.mqttConnectStatus) {
            MqttConnectStatus.CONNECTING -> {
                progressDialog.show()
            }
            MqttConnectStatus.CONNECTED -> {
                progressDialog.dismiss()
                Toast.makeText(
                    this,
                    "Kết nối với mqtt broker thành công",
                    Toast.LENGTH_SHORT
                ).show()
            }
            MqttConnectStatus.ERROR -> {
                progressDialog.dismiss()
                val msg = mqttConnectionStatusEvent.status.message
                msg?.let { message ->
                    if (message.isEmpty()) return@let
                    Toast.makeText(
                        this,
                        message,
                        Toast.LENGTH_SHORT
                    ).show()
                }

            }
        }
    }

}