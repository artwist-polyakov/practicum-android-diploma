package ru.practicum.android.diploma.vacancy.domain.models

sealed interface VacancyState {
    data class Content(
        val vacancy : VacancyItem,
        val isFavorite: Boolean
    ) : VacancyState
    object Loading : VacancyState
    object Empty : VacancyState
    object ConnectionError : VacancyState
}
