package com.thanhthido.androiddashboard.repository

import androidx.paging.PagingSource
import com.thanhthido.androiddashboard.data.remote.UrlsApi
import com.thanhthido.androiddashboard.data.remote.response.SensorData
import com.thanhthido.androiddashboard.repository.DashboardRepository.Companion.NETWORK_PAGE_SIZE
import okio.IOException
import retrofit2.HttpException

private const val STARTING_PAGE_INDEX = 1

class SensorDataPagingSource(
    private val api: UrlsApi,
    private val queryType: String
) : PagingSource<Int, SensorData>() {
    override suspend fun load(params: LoadParams<Int>): LoadResult<Int, SensorData> {
        val position = params.key ?: STARTING_PAGE_INDEX

        return try {
            val response = if (queryType == "all") {
                api.getAllSensorData(position, params.loadSize)
            } else {
                api.getSensorDataBasedOnType(queryType, position, params.loadSize)
            }
            val listOfData = response.sensorDataList ?: listOf()
            val nextKey = if (listOfData.isEmpty()) {
                null
            } else {
                position + (params.loadSize / NETWORK_PAGE_SIZE)
            }
            LoadResult.Page(
                data = listOfData,
                nextKey = nextKey,
                prevKey = if (position == STARTING_PAGE_INDEX) null else position - 1
            )
        } catch (e: IOException) {
            LoadResult.Error(e)
        } catch (e: HttpException) {
            LoadResult.Error(e)
        }

    }


}