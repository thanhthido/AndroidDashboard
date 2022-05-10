package com.thanhthido.androiddashboard.utils

enum class NetworkStatus {
    SUCCESS,
    LOADING,
    ERROR
}

data class NetworkResult<out T>(
    val networkStatus: NetworkStatus,
    val data: T?,
    val message: String?
) {
    companion object {

        fun <T> loading(data: T?): NetworkResult<T> {
            return NetworkResult(NetworkStatus.LOADING, data, null)
        }

        fun <T> error(msg: String?, data: T?): NetworkResult<T> {
            return NetworkResult(NetworkStatus.ERROR, data, msg)
        }

        fun <T> success(data: T?): NetworkResult<T> {
            return NetworkResult(NetworkStatus.SUCCESS, data, null)
        }

    }
}