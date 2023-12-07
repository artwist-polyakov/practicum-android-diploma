package ru.practicum.android.diploma.search.domain.api

import kotlinx.coroutines.flow.Flow
import ru.practicum.android.diploma.common.data.dto.Resource
import ru.practicum.android.diploma.search.domain.models.DetailedVacancyItem

interface SingleVacancyInteractor {
    suspend fun getVacancy(id: Int): Flow<Resource<DetailedVacancyItem>>
    suspend fun interactWithVacancyFavor(vacancyId: Int): Boolean
}
