package ru.practicum.android.diploma.common.domain.models

sealed class NetworkResult<T>(val data: T? = null, val error: NetworkErrors? = null) {
    class Success<T>(data: T) : NetworkResult<T>(data)
    class Error<T>(error: NetworkErrors, data: T? = null) : NetworkResult<T>(data, error)
}
