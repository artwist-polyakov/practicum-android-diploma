package ru.practicum.android.diploma.common.data.network.requests

data class SimilarVacanciesRequest(
    val vacancyId: Int,
    val page: Int = 0,
    val perPage: Int = 20,
)
