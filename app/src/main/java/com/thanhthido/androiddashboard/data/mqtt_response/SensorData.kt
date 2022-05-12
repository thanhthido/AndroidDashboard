package com.thanhthido.androiddashboard.data.mqtt_response


import com.google.gson.annotations.SerializedName

data class SensorData(
    @SerializedName("event")
    val event: String = "",
    @SerializedName("type")
    val type: String = "",
    @SerializedName("value")
    val value: Int = 0
)