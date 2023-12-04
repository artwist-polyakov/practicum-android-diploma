package ru.practicum.android.diploma.search.data.network

import kotlinx.coroutines.flow.Flow
import ru.practicum.android.diploma.common.data.dto.AreasDto
import ru.practicum.android.diploma.common.data.dto.IndustriesDto
import ru.practicum.android.diploma.common.data.dto.Resource
import ru.practicum.android.diploma.common.data.dto.VacancyItemDto
import ru.practicum.android.diploma.common.data.network.response.HHSearchResponse

interface HHSearchRepository {
    fun getVacancies(
        query: String,
        page: Int = 0,
        perPage: Int = 20,
        salary: Int? = null,
        onlyWithSalary: Boolean = false
    ): Flow<Resource<HHSearchResponse>>

    fun getVacancy(id: Int): Flow<Resource<VacancyItemDto>>
    fun getAreas(): Flow<Resource<List<AreasDto>>>
    fun getIndustries(): Flow<Resource<List<IndustriesDto>?>>
}
