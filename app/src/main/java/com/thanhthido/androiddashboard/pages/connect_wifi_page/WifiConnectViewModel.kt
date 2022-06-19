package com.thanhthido.androiddashboard.pages.connect_wifi_page

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.espressif.iot.esptouch.EsptouchTask
import com.espressif.iot.esptouch.IEsptouchResult
import com.thanhthido.androiddashboard.DashboardApplication
import com.thanhthido.androiddashboard.di.dispatchers.DispatcherProvider
import com.thanhthido.androiddashboard.utils.WifiConnectState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.async
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class WifiConnectViewModel @Inject constructor(
    private val dispatcherProvider: DispatcherProvider,
    application: Application
) : AndroidViewModel(application) {

    private val _wifiSmartConfig: MutableLiveData<WifiConnectState<IEsptouchResult>> =
        MutableLiveData()
    val wifiSmartConfig: LiveData<WifiConnectState<IEsptouchResult>> get() = _wifiSmartConfig

    fun connectWifiSmartConfig(
        ssid: String,
        bssid: String,
        password: String
    ) = viewModelScope.launch(dispatcherProvider.io) {
        _wifiSmartConfig.postValue(WifiConnectState.loading())

        try {
            val esptouchTask = async {
                val task = EsptouchTask(
                    ssid,
                    bssid,
                    password,
                    getApplication<DashboardApplication>().applicationContext
                ).also {
                    it.setPackageBroadcast(true)
                }
                task.executeForResult()
            }
            _wifiSmartConfig.postValue(WifiConnectState.connected(esptouchTask.await()))
        } catch (e: Exception) {
            _wifiSmartConfig.postValue(WifiConnectState.error(e.message))
        }
    }

}