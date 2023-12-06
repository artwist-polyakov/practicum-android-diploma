package ru.practicum.android.diploma.search.domain.api

import kotlinx.coroutines.flow.Flow
import ru.practicum.android.diploma.search.domain.models.SingleTreeElement
import ru.practicum.android.diploma.search.domain.models.VacanciesSearchResult

interface SearchInteractor {
    fun searchVacancies(
        text: String? = null,
        page: Int = 0,
        area: Int? = null,
        industry: Int? = null,
        salary: Int? = null,
        onlyWithSalary: Boolean = false
    ): Flow<VacanciesSearchResult>

    fun searchSimilarVacancies(
        vacancyId: Int,
        page: Int = 0
    ): Flow<VacanciesSearchResult>

    fun getIndustries(forId: Int? = null): Flow<List<SingleTreeElement>>

    fun getAreas(forId: Int? = null): Flow<List<SingleTreeElement>>

}
