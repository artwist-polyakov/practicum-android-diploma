package ru.practicum.android.diploma.favorites.domain.api

import ru.practicum.android.diploma.common.data.dto.VacancyWithEmployerDTO
import ru.practicum.android.diploma.search.domain.models.VacanciesSearchResult
import ru.practicum.android.diploma.vacancy.domain.models.DetailedVacancyItem

interface FavoritesDBConverter {
    fun map(from: List<VacancyWithEmployerDTO>, page: Int, found: Int, totalPages: Int): VacanciesSearchResult
    fun map(from: VacancyWithEmployerDTO): DetailedVacancyItem
}
