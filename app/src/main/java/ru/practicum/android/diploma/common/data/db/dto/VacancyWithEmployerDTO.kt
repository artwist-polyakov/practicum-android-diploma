package ru.practicum.android.diploma.common.data.db.dto

data class VacancyWithEmployerDTO(
    val vacancyId: Int,
    val title: String,
    val city: String?,
    val jobDescription: String?,
    val experience: String?,
    val employment: String?,
    val keySkillsJSON: String?,
    val contactName: String?,
    val contactEmail: String?,
    val phonesJSON: String?,
    val salaryFrom: Int?,
    val salaryTo: Int?,
    val currency: String?,
    val schedule: String?,
    val lastUpdate: Long,
    val employerId: Int,
    val logosJSON: String?,
    val employerName: String,
    val employerLogoUri: String?
)
