package ru.practicum.android.diploma.common.data.db.entity

import androidx.room.Entity
import androidx.room.ForeignKey

@Entity(
    tableName = "vacancy_employer_reference",
    primaryKeys = ["vacancyId"],
    foreignKeys = [
        ForeignKey(
            entity = EmployerEntity::class,
            parentColumns = ["id"],
            childColumns = ["employerId"],
            onDelete = ForeignKey.CASCADE,
        ),
        ForeignKey(
            entity = VacancyEntity::class,
            parentColumns = ["id"],
            childColumns = ["vacancyId"],
            onDelete = ForeignKey.CASCADE,
        )
    ]
)
data class VacancyEmployerReference(
    val employerId: Int,
    val vacancyId: Int
)
