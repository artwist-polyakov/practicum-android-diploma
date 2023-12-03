package ru.practicum.android.diploma.common.data.db.entity

import androidx.room.Entity
import androidx.room.PrimaryKey
import androidx.room.Relation

@Entity(tableName = "favourite_vacancy_table")
data class VacancyEntity(
    @PrimaryKey
    val id: Int,
    val jobDescription: String?,
    val jobTiming: String?, //например, "Полная занятость"
    val experience: String?, //например, "От 1 года до 3 лет"
    val keySkills: String?,
    val jobName: String,
    //зарплата:
    val currency: String,
    val from: String,
    val to: String,

    val schedule: String?, //например, "Полный день"
)
