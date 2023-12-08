package ru.practicum.android.diploma.search.domain.api

import kotlinx.coroutines.flow.Flow
import ru.practicum.android.diploma.common.domain.models.NetworkResult
import ru.practicum.android.diploma.search.domain.models.SingleTreeElement
import ru.practicum.android.diploma.search.domain.models.VacanciesSearchResult

interface SearchInteractor {
    suspend fun searchVacancies(
        text: String? = null,
        page: Int = 0,
        area: Int? = null,
        industry: String? = null,
        salary: Int? = null,
        onlyWithSalary: Boolean = false
    ): Flow<NetworkResult<VacanciesSearchResult>>

    fun searchSimilarVacancies(
        vacancyId: Int,
        page: Int = 0
    ): Flow<NetworkResult<VacanciesSearchResult>>

    fun getIndustries(forId: Int? = null): Flow<NetworkResult<List<SingleTreeElement>>>

    fun getAreas(forId: Int? = null): Flow<NetworkResult<List<SingleTreeElement>>>

}
