package ru.practicum.android.diploma.search.domain.models

data class DetailedVacancyItem(
    val id: Int,
    val title: String,
    val employerName: String?,
    val employerLogo: String?,
    val area: String?,
    val haveSalary: Boolean = false,
    val salaryFrom: Int? = null,
    val salaryTo: Int? = null,
    val salaryCurrency: String? = null,
    val experience: String?,
    val employment: String?,
    val schedule: String?,
    val description: String?,
    val keySkills: List<String>?,
    val favorite: Boolean = false
)
