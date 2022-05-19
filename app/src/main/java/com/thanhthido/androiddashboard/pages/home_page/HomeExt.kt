package com.thanhthido.androiddashboard.pages.home_page

import android.widget.Toast
import com.thanhthido.androiddashboard.utils.NetworkStatus
import timber.log.Timber
import java.text.SimpleDateFormat
import java.util.*
import kotlin.math.roundToInt

fun HomeFragment.subscribeLatestData(viewModel: HomeViewModel) {
    viewModel.latestData.observe(viewLifecycleOwner) { result ->
        when (result.networkStatus) {
            NetworkStatus.SUCCESS -> {
                result.data?.let { latestData ->
                    val date = Date(latestData.time * 1000)
                    val dateFormatter = SimpleDateFormat("dd/MM/yyyy", Locale.getDefault())
                    val timeFormatter = SimpleDateFormat("HH:mm:ss aa", Locale.getDefault())
                    binding.tvSubDate.text =
                        "${timeFormatter.format(date)}\n${dateFormatter.format(date)}"
                    binding.tvTempValue.text = "${latestData.tempValue} ℃" // 1
                    binding.tvCoValue.text = "${latestData.coValue.roundToInt()}"
                    binding.tvNo2Value.text = "${latestData.no2Value}" // 1
                    binding.tvCh4Value.text = "${latestData.ch4Value.roundToInt()}"
                    binding.tvPm1Value.text = "${latestData.pm1Value.roundToInt()}"
                    binding.tvPm25Value.text = "${latestData.pm25Value.roundToInt()}"
                    binding.tvPm10Value.text = "${latestData.pm10Value.roundToInt()}"

                }
            }
            NetworkStatus.ERROR -> {
                result.message?.let {
                    Timber.e(it)
                }
            }
            NetworkStatus.LOADING -> Unit
        }
    }
}