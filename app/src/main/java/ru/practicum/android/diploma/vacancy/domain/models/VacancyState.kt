package ru.practicum.android.diploma.vacancy.domain.models

sealed interface VacancyState {
    data class Content(
        val vacancy: DetailedVacancyItem,
    ) : VacancyState

    object Loading : VacancyState
    object Empty : VacancyState
    object ConnectionError : VacancyState
}
