package com.thanhthido.androiddashboard.pages.connect_wifi_page

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AlertDialog
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.navArgs
import com.thanhthido.androiddashboard.base.BaseFragment
import com.thanhthido.androiddashboard.databinding.FragmentConnectWifiBinding
import com.thanhthido.androiddashboard.pages.MainActivity
import com.thanhthido.androiddashboard.utils.WifiConnectStatus
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class WifiConnectFragment : BaseFragment<FragmentConnectWifiBinding>() {

    private val args: WifiConnectFragmentArgs by navArgs()
    private val viewModel: WifiConnectViewModel by viewModels()

    override fun inflateViewBinding(
        inflater: LayoutInflater,
        container: ViewGroup?
    ) = FragmentConnectWifiBinding.inflate(inflater, container, false)

    override fun initControls(savedInstanceState: Bundle?) {
        (requireActivity() as MainActivity).setSupportActionBar(binding.topBarWifi)

        subscribeEspConnect()
        executeEsptouch()
    }

    override fun initEvents() {
        binding.topBarWifi.setNavigationOnClickListener {
            requireActivity().onBackPressed()
        }
    }

    private fun subscribeEspConnect() {
        viewModel.wifiSmartConfig.observe(viewLifecycleOwner) { wifiConnectState ->
            when (wifiConnectState.wifiConnectStatus) {
                WifiConnectStatus.CONNECTED -> {
                    binding.progressView.visibility = View.GONE
                    wifiConnectState.data?.let { iEsptouchResult ->
                        if (iEsptouchResult.isCancelled) return@observe
                        if (!iEsptouchResult.isSuc) {
                            val dialog = AlertDialog.Builder(requireContext())
                                .setMessage("Kết nối thất bại")
                                .setPositiveButton(
                                    "Ok"
                                ) { viewDialog, _ ->
                                    viewDialog.dismiss()
                                }
                                .show()
                            dialog.setCanceledOnTouchOutside(false)
                            dialog.show()
                            return@observe
                        }

                        binding.messageView.text =
                            "Kết nối Wifi ${args.wifiDescription.wifiSSID} thành công"
                        val dialog = AlertDialog.Builder(requireContext())
                            .setMessage("Kết nối thành công")
                            .setPositiveButton(
                                "Ok"
                            ) { viewDialog, _ ->
                                viewDialog.dismiss()
                            }
                            .show()
                        dialog.setCanceledOnTouchOutside(false)
                        dialog.show()
                    }
                }
                WifiConnectStatus.ERROR -> {
                    binding.progressView.visibility = View.GONE
                    wifiConnectState.message?.let {
                        binding.messageView.text = "Kết nối thất bại"
                    }
                }
                WifiConnectStatus.CONNECTING -> {
                    binding.progressView.visibility = View.VISIBLE
                    binding.messageView.text = "Đang kết nối..."
                }
            }
        }
    }

    private fun executeEsptouch() {
        val wifi = args.wifiDescription
        val ssid = wifi.wifiSSID
        val bssid = wifi.wifiBSSID
        val password = wifi.wifiPassword
        viewModel.connectWifiSmartConfig(ssid, bssid, password)
    }

}
