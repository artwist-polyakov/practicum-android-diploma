package ru.practicum.android.diploma.common.data.dto

import ru.practicum.android.diploma.common.domain.models.NetworkErrors

sealed class Resource<T>(val data: T? = null, val error: NetworkErrors? = null) {
    class Success<T>(data: T) : Resource<T>(data)
    class Error<T>(error: NetworkErrors, data: T? = null) : Resource<T>(data, error)
}
