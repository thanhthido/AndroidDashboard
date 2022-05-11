package com.thanhthido.androiddashboard.pages.history_page

import android.os.Bundle
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import com.thanhthido.androiddashboard.base.BaseFragment
import com.thanhthido.androiddashboard.databinding.FragmentHistoryBinding
import com.thanhthido.androiddashboard.utils.NetworkStatus
import dagger.hilt.android.AndroidEntryPoint
import timber.log.Timber

@AndroidEntryPoint
class HistoryFragment : BaseFragment<FragmentHistoryBinding>() {

    private val viewModel: HistoryViewModel by viewModels()

    override fun inflateViewBinding(
        inflater: LayoutInflater,
        container: ViewGroup?
    ) = FragmentHistoryBinding.inflate(inflater, container, false)

    override fun initControls(savedInstanceState: Bundle?) {
        subscribeGetSensorData()
        viewModel.getAllSensorData(1, 10)
    }

    override fun initEvents() = Unit

    private fun subscribeGetSensorData() {
        viewModel.sensorDataResponse.observe(this) { networkResult ->
            when (networkResult.networkStatus) {
                NetworkStatus.SUCCESS -> {
                    networkResult.data?.let { sensorDataListResponse ->
                        binding.tvResult.text = "$sensorDataListResponse"
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