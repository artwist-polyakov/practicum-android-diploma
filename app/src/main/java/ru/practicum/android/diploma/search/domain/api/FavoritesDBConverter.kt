package ru.practicum.android.diploma.search.domain.api

import ru.practicum.android.diploma.common.data.dto.VacancyWithEmployerDTO
import ru.practicum.android.diploma.search.domain.models.VacanciesSearchResult

interface FavoritesDBConverter {
    fun map(from: VacancyWithEmployerDTO): Any
    fun map(from: List<VacancyWithEmployerDTO>, page: Int, found: Int, totalPages: Int): VacanciesSearchResult
}
