package ru.practicum.android.diploma.common.data.dto


//todo Добавить keyskills
data class VacancyItemDto(
    val id: Int,
    val name: String,
    val department: IdNameDto?,
    val experience: IdNameDto?,
    val employment: IdNameDto?,
    val schedule: IdNameDto?,
    val snippet: SnippetDto?,
    val salary: SalaryDto?,
    val employer: EmployerDto?,
)
