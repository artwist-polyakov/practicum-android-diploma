package ru.practicum.android.diploma.favorites.domain.api

import kotlinx.coroutines.flow.Flow
import ru.practicum.android.diploma.search.domain.models.VacanciesSearchResult
import ru.practicum.android.diploma.vacancy.domain.models.DetailedVacancyItem

interface FavoritesDBInteractor {
    suspend fun getFavoritesVacancies(page: Int = 0, limit: Int = 20): Flow<VacanciesSearchResult>
    suspend fun isVacancyFavorite(vacancyId: Int): Boolean
    suspend fun getVacancy(vacancyId: Int): Flow<DetailedVacancyItem?>
    suspend fun interactWithVacancyFavor(vacancy: DetailedVacancyItem): Boolean
}
