package ru.practicum.android.diploma.search.data

import kotlinx.coroutines.flow.Flow
import ru.practicum.android.diploma.common.data.dto.Resource
import ru.practicum.android.diploma.common.data.network.response.AreaSearchResponse
import ru.practicum.android.diploma.common.data.network.response.HHSearchResponse
import ru.practicum.android.diploma.common.data.network.response.IndustriesSearchResponse
import ru.practicum.android.diploma.common.data.network.response.SingleVacancyResponse

interface HHSearchRepository {
    fun getVacancies(
        query: String? = null,
        page: Int = 0,
        perPage: Int = 20,
        salary: Int? = null,
        area: Int? = null,
        industry: String? = null,
        onlyWithSalary: Boolean = false
    ): Flow<Resource<HHSearchResponse>>

    fun getVacancy(id: Int): Flow<Resource<SingleVacancyResponse>>

    fun getSimilarVacancies(
        id: Int,
        page: Int = 0,
        perPage: Int = 20
    ): Flow<Resource<HHSearchResponse>>

    fun getAreas(forId: Int? = null): Flow<Resource<AreaSearchResponse>>
    fun getIndustries(forId: Int? = null): Flow<Resource<IndustriesSearchResponse>>
}
