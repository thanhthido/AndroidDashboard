package com.thanhthido.androiddashboard.pages.home_page

import android.os.Bundle
import android.view.LayoutInflater
import android.view.ViewGroup
import com.thanhthido.androiddashboard.base.BaseFragment
import com.thanhthido.androiddashboard.databinding.FragmentHomeBinding

class HomeFragment : BaseFragment<FragmentHomeBinding>() {

    override fun inflateViewBinding(
        inflater: LayoutInflater,
        container: ViewGroup?
    ) = FragmentHomeBinding.inflate(inflater, container, false)

    override fun initControls(savedInstanceState: Bundle?) = Unit

    override fun initEvents() = Unit

}