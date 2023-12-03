package ru.practicum.android.diploma.common.data.network.responce

import ru.practicum.android.diploma.common.data.dto.Response
import ru.practicum.android.diploma.common.data.dto.VacancyItemDto

data class HHSearchResponse(
    val items: List<VacancyItemDto>,
    val found: Int,
    val pages: Int,
    val per_page: Int,
    val page: Int,
): Response()
