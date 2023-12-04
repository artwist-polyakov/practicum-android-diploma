package ru.practicum.android.diploma.common.data.network.response

import ru.practicum.android.diploma.common.data.dto.AreasDto
import ru.practicum.android.diploma.common.data.dto.Response

data class AreaSearchResponse(
    val areas: List<AreasDto>
) : Response()
