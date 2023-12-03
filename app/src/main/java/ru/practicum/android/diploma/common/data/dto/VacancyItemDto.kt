package ru.practicum.android.diploma.common.data.dto

data class VacancyItemDto(
    val id: Int,
    val name: String,
    val department: DepartmentDto?,
    val salary: SalaryDto?,
    val employer: EmployerDto?,
)
