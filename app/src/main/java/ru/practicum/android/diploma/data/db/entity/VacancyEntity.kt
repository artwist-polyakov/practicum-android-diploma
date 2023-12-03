package ru.practicum.android.diploma.data.db.entity

import androidx.room.Entity
import androidx.room.PrimaryKey
import androidx.room.Relation

@Entity(tableName = "favourite_vacancy_table")
data class VacancyEntity(
    @PrimaryKey
    val vacancyId: Int,
    val jobDescription: String?,
    val jobTiming: String?, //например, "Полная занятость"
    val experience: String?, //например, "От 1 года до 3 лет"
    val keySkills: String?,
    val jobName: String,
    val salary: String?,
    val schedule: String?, //например, "Полный день"
    @Relation(
        parentColumn = "employerId",
        entityColumn = "employerId"
    )
    val employerId:Int
)
