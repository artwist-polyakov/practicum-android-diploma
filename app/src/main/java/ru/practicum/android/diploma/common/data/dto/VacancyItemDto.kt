package ru.practicum.android.diploma.common.data.dto

import com.google.gson.annotations.SerializedName

data class VacancyItemDto(
    val id: Int,
    val name: String,
    val description: String?,
    val department: IdNameDto?,
    val experience: IdNameDto?,
    val employment: IdNameDto?,
    val schedule: IdNameDto?,
    val snippet: SnippetDto?,
    val salary: SalaryDto?,
    val employer: EmployerDto?,
    @SerializedName("key_skills") val keySkills: List<IdNameDto>?,
)
