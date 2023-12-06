package ru.practicum.android.diploma.search.domain.models

data class VacancyGeneral(
    val id: Int,
    val title: String,
    val employerName: String?,
    val employerLogo: String?,
    val haveSalary: Boolean = false,
    val salaryFrom: Int? = null,
    val salaryTo: Int? = null,
    val salaryCurrency: String? = null,
)
