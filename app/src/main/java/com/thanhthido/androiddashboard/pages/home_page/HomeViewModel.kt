package com.thanhthido.androiddashboard.pages.home_page

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.thanhthido.androiddashboard.data.remote.response.LatestSensorData
import com.thanhthido.androiddashboard.repository.DashboardRepository
import com.thanhthido.androiddashboard.utils.NetworkResult
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class HomeViewModel @Inject constructor(
    private val repository: DashboardRepository
) : ViewModel() {

    private val _latestData: MutableLiveData<NetworkResult<LatestSensorData>> = MutableLiveData()
    val latestData: LiveData<NetworkResult<LatestSensorData>> get() = _latestData

    fun getLatestData() = viewModelScope.launch {
        _latestData.postValue(NetworkResult.loading(null))
        val data = repository.getLatestData()
        _latestData.postValue(data)
    }

}