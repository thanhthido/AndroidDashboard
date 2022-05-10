package com.thanhthido.androiddashboard.data.remote.response


import com.google.gson.annotations.SerializedName

data class SensorDataListResponse(
    @SerializedName("page")
    val page: Int = 0,
    @SerializedName("sensorDataList")
    val sensorDataList: List<SensorData> = listOf(),
    @SerializedName("total")
    val total: Int = 0
)