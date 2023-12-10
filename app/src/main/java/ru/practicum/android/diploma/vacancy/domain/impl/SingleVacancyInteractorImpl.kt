package ru.practicum.android.diploma.vacancy.domain.impl

import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.withContext
import ru.practicum.android.diploma.common.data.db.AppDatabase
import ru.practicum.android.diploma.common.data.dto.Resource
import ru.practicum.android.diploma.search.data.network.HHSearchRepository
import ru.practicum.android.diploma.vacancy.domain.api.SingleVacancyConverter
import ru.practicum.android.diploma.vacancy.domain.api.SingleVacancyInteractor
import ru.practicum.android.diploma.vacancy.domain.models.DetailedVacancyItem

class SingleVacancyInteractorImpl(
    private val repository: HHSearchRepository,
    private val vacancyConverter: SingleVacancyConverter,
    private val db: AppDatabase,
    private val externalNavigator: ExternalNavigator
) : SingleVacancyInteractor {
    override suspend fun getVacancy(id: Int): Flow<Resource<DetailedVacancyItem>> {
        val isFavorite = isVacancyFavorite(id)
        return repository.getVacancy(id).map { vacancyConverter.map(it, isFavorite) }
    }

    private suspend fun isVacancyFavorite(vacancyId: Int): Boolean {
        return withContext(Dispatchers.IO) {
            db.vacancyDao().isVacancyExists(vacancyId)
        }
    }

    override suspend fun interactWithVacancyFavor(vacancyId: Int): Boolean {
        if (isVacancyFavorite(vacancyId)) {
            db.vacancyDao().removeVacancyById(vacancyId)
        } else {
            val vacancy = repository.getVacancy(vacancyId).first()
            if (vacancy is Resource.Success) {
                vacancy.data?.vacancy?.let {
                    val pair = vacancyConverter.map(it)
                    db.vacancyEmployerReferenceDao().addVacancy(pair.first, pair.second)
                    return true
                }
            }
        }
        return false
    }

    override fun shareVacancy(url: String) {
        externalNavigator.shareVacancy(url)
    }
}
