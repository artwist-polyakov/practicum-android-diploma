package ru.practicum.android.diploma.common.data.db.entity

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "favorites_vacancies_table")
data class VacancyEntity(
    @PrimaryKey
    val id: Int,
    val title: String,
    val city: String,
    val jobDescription: String?,
    val experience: String?,
    val employment: String?,
    val keySkillsJSON: String?,
    val salaryFrom: Int?,
    val salaryTo: Int?,
    val currency: String,
    val schedule: String?,
    val lastUpdate: Long
)
