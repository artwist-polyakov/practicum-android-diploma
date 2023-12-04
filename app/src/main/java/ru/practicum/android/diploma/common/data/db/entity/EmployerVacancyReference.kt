package ru.practicum.android.diploma.common.data.db.entity

import androidx.room.Entity
import androidx.room.ForeignKey

@Entity(
    tableName = "employer_vacancy_reference",
    primaryKeys = ["employerId", "vacancyId"],
    foreignKeys = [
        ForeignKey(
            entity= EmployerEntity::class,
            parentColumns = ["id"],
            childColumns = ["employerId"],
            onDelete = ForeignKey.CASCADE,
            onUpdate = ForeignKey.CASCADE
        ),
        ForeignKey(
            entity= VacancyEntity::class,
            parentColumns = ["id"],
            childColumns = ["vacancyId"],
            onDelete = ForeignKey.CASCADE,
            onUpdate = ForeignKey.CASCADE
        )
    ]
)
data class EmployerVacancyReference (
    val employerId:Int,
    val vacancyId:Int,
    val lastUpdate: Long?
)
