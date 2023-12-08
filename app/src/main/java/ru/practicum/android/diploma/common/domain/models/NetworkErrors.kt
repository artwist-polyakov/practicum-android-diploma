package ru.practicum.android.diploma.common.domain.models

sealed class NetworkErrors {
    object NoInternet : NetworkErrors()
    object ServerError : NetworkErrors()
    object UnknownError : NetworkErrors()
    object Forbidden : NetworkErrors()
    object ClientError : NetworkErrors()
}
