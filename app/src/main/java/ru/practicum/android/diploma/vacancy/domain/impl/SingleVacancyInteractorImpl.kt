package ru.practicum.android.diploma.vacancy.domain.impl

import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.withContext
import ru.practicum.android.diploma.common.data.db.AppDatabase
import ru.practicum.android.diploma.common.data.dto.Resource
import ru.practicum.android.diploma.common.data.dto.VacancyItemDto
import ru.practicum.android.diploma.search.data.HHSearchRepository
import ru.practicum.android.diploma.vacancy.domain.api.ExternalNavigator
import ru.practicum.android.diploma.vacancy.domain.api.SingleVacancyConverter
import ru.practicum.android.diploma.vacancy.domain.api.SingleVacancyInteractor
import ru.practicum.android.diploma.vacancy.domain.models.DetailedVacancyItem

class SingleVacancyInteractorImpl(
    private val repository: HHSearchRepository,
    private val vacancyConverter: SingleVacancyConverter,
    private val db: AppDatabase,
    private val externalNavigator: ExternalNavigator
) : SingleVacancyInteractor {

    private var currentVacancy: VacancyItemDto? = null
    override suspend fun getVacancy(id: Int): Flow<Resource<DetailedVacancyItem>> {
        val isFavorite = isVacancyFavorite(id)

        // todo забрать этот код в интерактор избранного

//                return db.vacancyEmployerReferenceDao().getVacancyWithEmployer(id).map {
//                    it.let {
//                        it?.let { vacancyConverter.map(it, isFavorite) }
//                            ?: Resource.Error(NetworkErrors.UnknownError)
//                    }
//                }
//            }
        return repository.getVacancy(id).map {
            currentVacancy = it.data?.vacancy
            vacancyConverter.map(it, isFavorite) }
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
            currentVacancy?.let {
                val pair = vacancyConverter.map(it)
                db.vacancyEmployerReferenceDao().addVacancy(pair.first, pair.second)
                return true
            }
        }
        return false
    }

    override fun shareVacancy(url: String) {
        externalNavigator.shareVacancy(url)
    }
}
