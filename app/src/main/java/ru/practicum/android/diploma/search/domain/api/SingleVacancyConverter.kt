package ru.practicum.android.diploma.search.domain.api

import ru.practicum.android.diploma.common.data.db.entity.EmployerEntity
import ru.practicum.android.diploma.common.data.db.entity.VacancyEntity
import ru.practicum.android.diploma.common.data.dto.Resource
import ru.practicum.android.diploma.common.data.dto.VacancyItemDto
import ru.practicum.android.diploma.common.data.dto.VacancyWithEmployerDTO
import ru.practicum.android.diploma.common.data.network.response.SingleVacancyResponse
import ru.practicum.android.diploma.search.domain.models.DetailedVacancyItem

interface SingleVacancyConverter {
    fun map(from: Resource<SingleVacancyResponse>, isFavorite: Boolean): Resource<DetailedVacancyItem>
    fun map(from: VacancyWithEmployerDTO, isFavorite: Boolean): Resource<DetailedVacancyItem>
    fun map(from: VacancyItemDto): Pair<VacancyEntity, EmployerEntity?>
}
