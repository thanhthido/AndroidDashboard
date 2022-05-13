package com.thanhthido.androiddashboard.pages

import android.app.ActivityManager
import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.ui.setupWithNavController
import com.thanhthido.androiddashboard.R
import com.thanhthido.androiddashboard.databinding.ActivityMainBinding
import com.thanhthido.androiddashboard.service.DashboardService
import dagger.hilt.android.AndroidEntryPoint


@Suppress("DEPRECATION")
@AndroidEntryPoint
class MainActivity : AppCompatActivity() {

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

}