package ru.practicum.android.diploma.common.data.db.entity

import androidx.room.Entity
import androidx.room.PrimaryKey
import androidx.room.Relation

@Entity(tableName = "favourite_vacancy_table")
data class VacancyEntity(
    @PrimaryKey
    val id: Int,
    val city: String,
    val jobDescription: String?,
    val jobTiming: String?,
    val experience: String?,
    val keySkills: List<String>?,
    val professional_roles: List<String>?,
    val jobName: String,
    val currency: String,
    val from: String,
    val to: String,
    val schedule: String?,
)
