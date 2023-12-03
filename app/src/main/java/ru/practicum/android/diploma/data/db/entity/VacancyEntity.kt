package ru.practicum.android.diploma.data.db.entity

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "favourite_vacancy_table")
data class VacancyEntity(
    @PrimaryKey
    val vacancyId: Int,
    val employerEmail: String?,
    val employerContactName: String?,
    val employerPhone: String?,
    val jobDescription: String?,
    val employerLogo_urls_90: String?,
    val employerLogo_urls_240: String?,
    val employerLogo_urls_original: String?,
    val employerName: String,
    val jobTiming: String?, //например, "Полная занятость"
    val experience: String?, //например, "От 1 года до 3 лет"
    val keySkills: String?,
    val jobName: String,
    val salary: String?,
    val schedule: String?, //например, "Полный день"
)
