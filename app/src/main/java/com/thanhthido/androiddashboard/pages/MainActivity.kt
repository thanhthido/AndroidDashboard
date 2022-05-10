package com.thanhthido.androiddashboard.pages

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.TextView
import androidx.activity.viewModels
import com.thanhthido.androiddashboard.R
import com.thanhthido.androiddashboard.pages.home_page.HomeViewModel
import com.thanhthido.androiddashboard.utils.NetworkStatus
import dagger.hilt.android.AndroidEntryPoint
import timber.log.Timber

@AndroidEntryPoint
class MainActivity : AppCompatActivity() {

    private val viewModel: HomeViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        subscribeGetSensorData()
        viewModel.getAllSensorData(1, 10)
    }

    private fun subscribeGetSensorData() {
        viewModel.sensorDataResponse.observe(this) { networkResult ->
            when (networkResult.networkStatus) {
                NetworkStatus.SUCCESS -> {
                    networkResult.data?.let { sensorDataListResponse ->
                        findViewById<TextView>(R.id.tv_result).text = "$sensorDataListResponse"
                    }
                }
                NetworkStatus.ERROR -> {
                    Timber.e(networkResult.message)
                }
                NetworkStatus.LOADING -> Unit
            }
        }
    }

}