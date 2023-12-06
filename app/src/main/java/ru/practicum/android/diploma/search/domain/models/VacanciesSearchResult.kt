package ru.practicum.android.diploma.search.domain.models

data class VacanciesSearchResult(
    val vacanciesFound: Int,
    val totalPages: Int,
    val currentPage: Int,
    val vacancies: List<VacancyGeneral>
)
