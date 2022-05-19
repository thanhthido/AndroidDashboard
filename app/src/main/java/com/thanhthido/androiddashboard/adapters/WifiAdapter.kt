package com.thanhthido.androiddashboard.adapters

import android.net.wifi.ScanResult
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.espressif.iot.esptouch2.provision.TouchNetUtil
import com.thanhthido.androiddashboard.databinding.ItemWifiBinding
import com.thanhthido.androiddashboard.events.WifiAdapterClick
import org.greenrobot.eventbus.EventBus

class WifiAdapter : RecyclerView.Adapter<WifiAdapter.WifiViewHolder>() {

    private val listOfWifi = mutableListOf<ScanResult>()

    fun setData(list: List<ScanResult>) {
        listOfWifi.clear()
        listOfWifi.addAll(list)
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): WifiViewHolder {
        return WifiViewHolder(
            ItemWifiBinding.inflate(
                LayoutInflater.from(parent.context), parent,
                false
            )
        )
    }

    override fun onBindViewHolder(holder: WifiViewHolder, position: Int) {
        val item = listOfWifi[position]
        holder.bindData(item)
    }

    override fun getItemCount() = listOfWifi.size

    inner class WifiViewHolder(private val binding: ItemWifiBinding) :
        RecyclerView.ViewHolder(binding.root) {

        private var wifi: ScanResult? = null
        private var is5G: Boolean = false

        init {
            binding.root.setOnClickListener {
                EventBus.getDefault().post(WifiAdapterClick(wifi!!, is5G))
            }
        }

        fun bindData(scanResult: ScanResult) {
            wifi = scanResult

            binding.tvWifiName.text = scanResult.SSID

            val msg = if (!TouchNetUtil.is5G(scanResult.frequency)) {
                is5G = false
                "Wi-Fi 2.4GHz - ${scanResult.frequency}"
            } else {
                is5G = true
                "Wi-Fi 5GHz - ${scanResult.frequency}"

            }

            binding.tvWifiStrength.text = msg

        }
    }

}