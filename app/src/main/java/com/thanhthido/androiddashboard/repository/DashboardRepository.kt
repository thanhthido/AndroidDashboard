package com.thanhthido.androiddashboard.repository

import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.liveData
import com.thanhthido.androiddashboard.data.remote.UrlsApi
import javax.inject.Inject

class DashboardRepository @Inject constructor(
    private val api: UrlsApi
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

}