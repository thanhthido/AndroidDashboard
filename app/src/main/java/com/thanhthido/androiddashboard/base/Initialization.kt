package com.thanhthido.androiddashboard.base

import android.os.Bundle

interface Initialization {

    // Call api, set du lieu
    fun initControls(savedInstanceState: Bundle?)

    // Click, tap listener
    fun initEvents()
}