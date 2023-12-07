package ru.practicum.android.diploma.search.domain.impl

import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import ru.practicum.android.diploma.common.data.db.AppDatabase
import ru.practicum.android.diploma.search.domain.api.FavoritesDBConverter
import ru.practicum.android.diploma.search.domain.api.FavoritesDBInteractor
import ru.practicum.android.diploma.search.domain.models.VacanciesSearchResult
import java.lang.Math.ceil

class FavoritesDBInteractorImpl(
    val database: AppDatabase,
    val converter: FavoritesDBConverter
) : FavoritesDBInteractor {
    override suspend fun getFavoritesVacancies(page: Int, limit: Int): Flow<VacanciesSearchResult> {
        val total = database.vacancyDao().getVacanciesCount()
        val totalPages = ceil(total.toDouble() / limit).toInt()
        return database.vacancyEmployerReferenceDao().getVacancies(page, limit).map {
            converter.map(it, page, total, totalPages)
        }
    }

    override suspend fun isVacancyFavorite(vacancyId: Int): Boolean =
        database.vacancyDao().isVacancyExists(vacancyId)
}
