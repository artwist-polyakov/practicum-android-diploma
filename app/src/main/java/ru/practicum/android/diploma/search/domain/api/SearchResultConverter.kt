package ru.practicum.android.diploma.search.domain.api

import ru.practicum.android.diploma.common.data.dto.AreasDto
import ru.practicum.android.diploma.common.data.dto.IndustriesDto
import ru.practicum.android.diploma.common.data.dto.Resource
import ru.practicum.android.diploma.common.data.dto.VacancyItemDto
import ru.practicum.android.diploma.common.data.network.response.AreaSearchResponse
import ru.practicum.android.diploma.common.data.network.response.HHSearchResponse
import ru.practicum.android.diploma.common.data.network.response.IndustriesSearchResponse
import ru.practicum.android.diploma.search.domain.models.SingleTreeElement
import ru.practicum.android.diploma.search.domain.models.VacanciesSearchResult
import ru.practicum.android.diploma.search.domain.models.VacancyGeneral

interface SearchResultConverter {
    fun map(from: Resource<HHSearchResponse>): Resource<VacanciesSearchResult>
    fun map(from: Resource<AreaSearchResponse>): Resource<List<SingleTreeElement>>
    fun map(from: Resource<IndustriesSearchResponse>): Resource<List<SingleTreeElement>>
    fun map(from: VacancyItemDto): VacancyGeneral
    fun map(from: AreasDto): SingleTreeElement
    fun map(from: IndustriesDto): SingleTreeElement
}
