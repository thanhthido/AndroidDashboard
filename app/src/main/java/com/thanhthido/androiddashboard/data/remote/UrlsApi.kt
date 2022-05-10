package com.thanhthido.androiddashboard.data.remote

import com.thanhthido.androiddashboard.data.remote.response.SensorDataListResponse
import com.thanhthido.androiddashboard.utils.Constant.PATH_GET_ALL_SENSOR_DATA
import com.thanhthido.androiddashboard.utils.Constant.PATH_GET_SENSOR_DATA_BASED_ON_TYPE
import com.thanhthido.androiddashboard.utils.Constant.QUERY_LIMIT
import com.thanhthido.androiddashboard.utils.Constant.QUERY_PAGE
import com.thanhthido.androiddashboard.utils.Constant.QUERY_TYPE
import retrofit2.http.GET
import retrofit2.http.Query

interface UrlsApi {

    @GET(PATH_GET_ALL_SENSOR_DATA)
    suspend fun getAllSensorData(
        @Query(QUERY_PAGE) page: Int = 1,
        @Query(QUERY_LIMIT) limit: Int = 10
    ): SensorDataListResponse

    @GET(PATH_GET_SENSOR_DATA_BASED_ON_TYPE)
    suspend fun getSensorDataBasedOnType(
        @Query(QUERY_TYPE) type: String = "temperature",
        @Query(QUERY_PAGE) page: Int = 1,
        @Query(QUERY_LIMIT) limit: Int = 10
    ): SensorDataListResponse

}

