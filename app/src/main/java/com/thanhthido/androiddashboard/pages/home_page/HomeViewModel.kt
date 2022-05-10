package com.thanhthido.androiddashboard.pages.home_page

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.thanhthido.androiddashboard.data.remote.response.SensorDataListResponse
import com.thanhthido.androiddashboard.repository.DashboardRepository
import com.thanhthido.androiddashboard.utils.NetworkResult
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class HomeViewModel @Inject constructor(
    private val repository: DashboardRepository
) : ViewModel() {

    private var _sensorDataResponse: MutableLiveData<NetworkResult<SensorDataListResponse>> =
        MutableLiveData()
    val sensorDataResponse: LiveData<NetworkResult<SensorDataListResponse>> = _sensorDataResponse

    fun getAllSensorData(
        page: Int = 1,
        limit: Int = 10
    ) = viewModelScope.launch {
        val response = repository.getAllSensorData(page, limit)
        _sensorDataResponse.postValue(response)
    }

}