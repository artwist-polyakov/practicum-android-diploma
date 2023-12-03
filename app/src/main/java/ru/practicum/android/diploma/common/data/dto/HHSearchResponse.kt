package ru.practicum.android.diploma.common.data.dto

data class HHSearchResponse(
    val items: List<VacancyItemDto>,
    val found: Int,
    val pages: Int,
    val per_page: Int,
    val page: Int,
): Response ()
