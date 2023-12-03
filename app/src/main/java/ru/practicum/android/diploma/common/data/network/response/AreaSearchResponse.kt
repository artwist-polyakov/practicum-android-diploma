package ru.practicum.android.diploma.common.data.network.response

import ru.practicum.android.diploma.common.data.dto.AreaDto
import ru.practicum.android.diploma.common.data.dto.Response

data class AreaSearchResponse(
    val areas: List<AreaDto>
): Response()
