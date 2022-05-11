package com.thanhthido.androiddashboard.pages.wifi_page

import android.os.Bundle
import android.view.LayoutInflater
import android.view.ViewGroup
import com.thanhthido.androiddashboard.base.BaseFragment
import com.thanhthido.androiddashboard.databinding.FragmentWifiBinding

class WifiFragment : BaseFragment<FragmentWifiBinding>() {

    override fun inflateViewBinding(
        inflater: LayoutInflater,
        container: ViewGroup?
    ) = FragmentWifiBinding.inflate(inflater, container, false)

    override fun initControls(savedInstanceState: Bundle?) = Unit

    override fun initEvents() = Unit

}