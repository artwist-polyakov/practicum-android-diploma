package ru.practicum.android.diploma.search.domain.api

import kotlinx.coroutines.flow.Flow
import ru.practicum.android.diploma.common.data.dto.Resource
import ru.practicum.android.diploma.search.domain.models.Industry
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
    ): Flow<Resource<VacanciesSearchResult>>

    fun searchSimilarVacancies(
        vacancyId: Int,
        page: Int = 0
    ): Flow<Resource<VacanciesSearchResult>>

    fun getIndustries(forId: Int? = null): Flow<Resource<List<Industry>>>

    fun getAreas(forId: Int? = null): Flow<Resource<List<SingleTreeElement>>>

}
