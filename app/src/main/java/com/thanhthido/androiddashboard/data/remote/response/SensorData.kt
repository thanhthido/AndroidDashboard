package com.thanhthido.androiddashboard.data.remote.response


import com.google.gson.annotations.SerializedName

data class SensorData(
    @SerializedName("event")
    val event: String = "",
    @SerializedName("_id")
    val id: String = "",
    @SerializedName("time")
    val time: Long = 0,
    @SerializedName("type")
    val type: String = "",
    @SerializedName("value")
    val value: Double = 0.0,
    @SerializedName("msg")
    val msg: String = ""
)