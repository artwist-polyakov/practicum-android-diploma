package ru.practicum.android.diploma.search.domain.impl

import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import ru.practicum.android.diploma.common.data.dto.Resource
import ru.practicum.android.diploma.search.data.HHSearchRepository
import ru.practicum.android.diploma.search.domain.api.SearchInteractor
import ru.practicum.android.diploma.search.domain.api.SearchResultConverter
import ru.practicum.android.diploma.search.domain.models.Industry
import ru.practicum.android.diploma.search.domain.models.SingleTreeElement
import ru.practicum.android.diploma.search.domain.models.VacanciesSearchResult

class SearchInteractorImpl(
    private val repository: HHSearchRepository,
    private val converter: SearchResultConverter
) : SearchInteractor {
    override suspend fun searchVacancies(
        text: String?,
        page: Int,
        area: Int?,
        industry: String?,
        salary: Int?,
        onlyWithSalary: Boolean
    ): Flow<Resource<VacanciesSearchResult>> = repository.getVacancies(
        query = text,
        page = page,
        area = area,
        industry = industry,
        salary = salary,
        onlyWithSalary = onlyWithSalary
    ).map { result ->
        converter.mapSearchResponce(result)
    }

    override fun searchSimilarVacancies(vacancyId: Int, page: Int): Flow<Resource<VacanciesSearchResult>> =
        repository.getSimilarVacancies(
            id = vacancyId,
            page = page
        ).map { result ->
            converter.mapSearchResponce(result)
        }

    override fun getIndustries(): Flow<Resource<List<Industry>>> =
        repository.getIndustries().map { result ->
            converter.mapIndustriesResponse(result)
        }

    override fun getAreas(forId: Int?): Flow<Resource<List<SingleTreeElement>>> =
        repository.getAreas(forId).map { result ->
            converter.mapAreaResponse(result)
        }
}
