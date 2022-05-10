package com.thanhthido.androiddashboard.repository

import com.thanhthido.androiddashboard.data.remote.UrlsApi
import com.thanhthido.androiddashboard.di.dispatchers.DispatcherProvider
import com.thanhthido.androiddashboard.utils.NetworkResult
import kotlinx.coroutines.withContext
import javax.inject.Inject

class DashboardRepository @Inject constructor(
    private val urlsApi: UrlsApi,
    private val dispatchers: DispatcherProvider
) {

    suspend fun getAllSensorData(
        page: Int = 1,
        limit: Int = 10
    ) = withContext(dispatchers.io) {
        try {
            val response = urlsApi.getAllSensorData(page, limit)
            NetworkResult.success(response)
        } catch (e: Exception) {
            NetworkResult.error(e.message, null)
        }
    }

    suspend fun getSensorDataBasedOnType(
        type: String = "temperature",
        page: Int = 1,
        limit: Int = 10
    ) = withContext(dispatchers.io) {
        try {
            val response = urlsApi.getSensorDataBasedOnType(type, page, limit)
            NetworkResult.success(response)
        } catch (e: Exception) {
            NetworkResult.error(e.message, null)
        }
    }

}