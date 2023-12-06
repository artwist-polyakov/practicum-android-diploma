package ru.practicum.android.diploma.search.data.network

import kotlinx.coroutines.flow.Flow
import ru.practicum.android.diploma.common.data.dto.Resource
import ru.practicum.android.diploma.common.data.network.response.AreaSearchResponse
import ru.practicum.android.diploma.common.data.network.response.HHSearchResponse
import ru.practicum.android.diploma.common.data.network.response.IndustriesSearchResponse
import ru.practicum.android.diploma.common.data.network.response.SingleVacancyResponse

interface HHSearchRepository {
    fun getVacancies(
        query: String,
        page: Int = 0,
        perPage: Int = 20,
        salary: Int? = null,
        onlyWithSalary: Boolean = false
    ): Flow<Resource<HHSearchResponse>>

    fun getVacancy(id: Int): Flow<Resource<SingleVacancyResponse>>

    fun getSimilarVacancies(
        id: Int,
        page: Int = 0,
        perPage: Int = 20
    ): Flow<Resource<HHSearchResponse>>

    fun getAreas(): Flow<Resource<AreaSearchResponse>>
    fun getIndustries(): Flow<Resource<IndustriesSearchResponse>>
}
