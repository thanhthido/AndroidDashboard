package com.thanhthido.androiddashboard.data.remote.response


import com.google.gson.annotations.SerializedName

data class LatestSensorData(
    @SerializedName("ch4Value")
    val ch4Value: Double = 0.0,
    @SerializedName("coValue")
    val coValue: Double = 0.0,
    @SerializedName("no2Value")
    val no2Value: Double = 0.0,
    @SerializedName("pm10Value")
    val pm10Value: Double = 0.0,
    @SerializedName("pm1Value")
    val pm1Value: Double = 0.0,
    @SerializedName("pm25Value")
    val pm25Value: Double = 0.0,
    @SerializedName("tempValue")
    val tempValue: Double = 0.0,
    @SerializedName("time")
    val time: Long = 0
)