package ru.practicum.android.diploma.search.domain.api

import kotlinx.coroutines.flow.Flow
import ru.practicum.android.diploma.search.domain.models.VacanciesSearchResult

interface FavoritesDBInteractor {
    suspend fun getFavoritesVacancies(page: Int = 0, limit: Int = 20): Flow<VacanciesSearchResult>
    suspend fun isVacancyFavorite(vacancyId: Int): Boolean
}
