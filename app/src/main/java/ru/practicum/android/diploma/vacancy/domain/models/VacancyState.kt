package ru.practicum.android.diploma.vacancy.domain.models

import ru.practicum.android.diploma.search.domain.models.DetailedVacancyItem

sealed interface VacancyState {
    data class Content(
        val vacancy: DetailedVacancyItem,
    ) : VacancyState

    object Loading : VacancyState
    object Empty : VacancyState
    object ConnectionError : VacancyState
}
