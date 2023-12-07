package ru.practicum.android.diploma.vacancy.domain.models

/**
 * DTO для отдельной вакансии
 *
 * - employment — тут Полная занятость или удаленная работа
 * - schedule — тут график, например "Полный день"
 * - employer — содержит name имя работодателя
 */
data class VacancyItem(
    val id: Int,
    val name: String,
    val description: String?,
    val department: IdName?,
    val experience: IdName?,
    val employment: IdName?,
    val area: IdName?,
    val schedule: IdName?,
    val snippet: Snippet?,
    val salary: Salary?,
    val employer: Employer?,
    val contacts: Contacts?,
    val keySkills: List<IdName>?,
)
