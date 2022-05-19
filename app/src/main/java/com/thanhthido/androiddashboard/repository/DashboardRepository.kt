package com.thanhthido.androiddashboard.repository

import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.liveData
import com.thanhthido.androiddashboard.data.remote.UrlsApi
import com.thanhthido.androiddashboard.di.dispatchers.DispatcherProvider
import com.thanhthido.androiddashboard.utils.NetworkResult
import com.thanhthido.androiddashboard.utils.NetworkStatus
import kotlinx.coroutines.withContext
import javax.inject.Inject

class DashboardRepository @Inject constructor(
    private val api: UrlsApi,
    private val dispatchers: DispatcherProvider
) {

    companion object {
        const val NETWORK_PAGE_SIZE = 21
    }

    fun getSearchSensorData(
        query: String = "{\"first\":\"all\",\"second\":\"all\"}"
    ) = Pager(
        config = PagingConfig(
            pageSize = NETWORK_PAGE_SIZE,
            enablePlaceholders = false
        ),
        pagingSourceFactory = {
            SensorDataPagingSource(
                api,
                query
            )
        }
    ).liveData

    suspend fun getLatestData() = withContext(dispatchers.io) {
        try {
            val result = api.getLatestData()
            NetworkResult.success(result)
        } catch (e: Exception) {
            NetworkResult.error(e.message, null)
        }
    }

}