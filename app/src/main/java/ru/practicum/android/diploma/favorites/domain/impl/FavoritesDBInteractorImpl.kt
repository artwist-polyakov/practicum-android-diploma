package ru.practicum.android.diploma.favorites.domain.impl

import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.withContext
import ru.practicum.android.diploma.common.data.db.AppDatabase
import ru.practicum.android.diploma.common.data.dto.Resource
import ru.practicum.android.diploma.favorites.domain.api.FavoritesDBConverter
import ru.practicum.android.diploma.favorites.domain.api.FavoritesDBInteractor
import ru.practicum.android.diploma.search.data.HHSearchRepository
import ru.practicum.android.diploma.search.domain.models.VacanciesSearchResult
import ru.practicum.android.diploma.vacancy.domain.api.SingleVacancyConverter
import ru.practicum.android.diploma.vacancy.domain.models.DetailedVacancyItem
import java.lang.Math.ceil

class FavoritesDBInteractorImpl(
    val database: AppDatabase,
    val converter: FavoritesDBConverter,
    val repository: HHSearchRepository,
    val converterSingle: SingleVacancyConverter
) : FavoritesDBInteractor {
    override suspend fun getFavoritesVacancies(page: Int, limit: Int): Flow<VacanciesSearchResult> {

        val total = withContext(Dispatchers.IO) { database.vacancyDao().getVacanciesCount() }
        val totalPages = ceil(total.toDouble() / limit).toInt()
        return withContext(Dispatchers.IO) {
            database.vacancyEmployerReferenceDao().getVacancies(page, limit).map {
                converter.map(it, page, total, totalPages)
            }
        }
    }

    override suspend fun isVacancyFavorite(vacancyId: Int): Boolean =
        withContext(Dispatchers.IO) { database.vacancyDao().isVacancyExists(vacancyId) }

    override suspend fun getVacancy(vacancyId: Int): Flow<DetailedVacancyItem?> {
        return withContext(Dispatchers.IO) {
            database.vacancyEmployerReferenceDao().getVacancyWithEmployer(vacancyId).map {
                it?.let { vacancy ->
                    converter.map(vacancy)
                }
            }
        }
    }

    override suspend fun interactWithVacancyFavor(vacancy: DetailedVacancyItem): Boolean {
        if (isVacancyFavorite(vacancy.id)) {
            database.vacancyDao().removeVacancyById(vacancy.id)
        } else {
            val vacancy = repository.getVacancy(vacancy.id).first()
            if (vacancy is Resource.Success) {
                vacancy.data?.vacancy?.let {
                    val pair = converterSingle.map(it)
                    database.vacancyEmployerReferenceDao().addVacancy(pair.first, pair.second)
                    return true
                }
            }
        }
        return false
    }
}
