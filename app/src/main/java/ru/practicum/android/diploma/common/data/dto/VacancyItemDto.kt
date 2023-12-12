package ru.practicum.android.diploma.common.data.dto

import com.google.gson.annotations.SerializedName
import ru.practicum.android.diploma.vacancy.domain.models.Contacts

/**
 * DTO для отдельной вакансии
 *
 * - employment — тут Полная занятость или удаленная работа
 * - schedule — тут график, например "Полный день"
 * - employer — содержит name имя работодателя
 */
data class VacancyItemDto(
    val id: Int,
    val name: String,
    val description: String?,
    val department: IdNameDto?,
    val experience: IdNameDto?,
    val employment: IdNameDto?,
    val area: IdNameDto?,
    val schedule: IdNameDto?,
    val snippet: SnippetDto?,
    val salary: SalaryDto?,
    val employer: EmployerDto?,
    val contacts: ContactsDto?,
    @SerializedName("key_skills") val keySkills: List<IdNameDto>?,
)
