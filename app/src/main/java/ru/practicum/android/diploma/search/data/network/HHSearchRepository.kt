package ru.practicum.android.diploma.search.data.network

import kotlinx.coroutines.flow.Flow
import ru.practicum.android.diploma.common.domain.models.NetworkResult
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
    ): Flow<NetworkResult<HHSearchResponse>>

    fun getVacancy(id: Int): Flow<NetworkResult<SingleVacancyResponse>>

    fun getSimilarVacancies(
        id: Int,
        page: Int = 0,
        perPage: Int = 20
    ): Flow<NetworkResult<HHSearchResponse>>

    fun getAreas(forId: Int? = null): Flow<NetworkResult<AreaSearchResponse>>
    fun getIndustries(forId: Int? = null): Flow<NetworkResult<IndustriesSearchResponse>>
}
