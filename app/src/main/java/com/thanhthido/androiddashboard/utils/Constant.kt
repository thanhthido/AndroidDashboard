package com.thanhthido.androiddashboard.utils

object Constant {

    const val BASE_URL = "http://172.16.3.97:8001"
    const val PATH_GET_ALL_SENSOR_DATA = "/sensorsData"
    const val PATH_GET_SENSOR_DATA_BASED_ON_TYPE = "/sensorsData/data"

    // query param
    const val QUERY_PAGE = "page"
    const val QUERY_LIMIT = "limit"
    const val QUERY_TYPE = "type"

    // shared pref
    const val ENCRYPTED_SHARED_PREF = "androidDashboard_encrypted"

    const val NORMAL_MODE = "normal"
    const val ERROR_MODE = "error"
}