package com.thanhthido.androiddashboard.repository

import androidx.paging.PagingSource
import com.google.gson.Gson
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

        val query = Gson().fromJson<Pair<String, String>>(queryType, Pair::class.java)
        val (type, event) = query

        return try {
            val response = if (type == "all" && event == "all") {
                api.getAllSensorData(position, params.loadSize)
            } else if (type == "all" && event == "normal") {
                api.getAllSensorData(position, params.loadSize, "normal")
            } else if (type == "all" && event == "error") {
                api.getAllSensorData(position, params.loadSize, "error")
            } else {
                api.getSensorDataBasedOnType(type, position, params.loadSize, event)
            }
            val listOfData = response.sensorDataList
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