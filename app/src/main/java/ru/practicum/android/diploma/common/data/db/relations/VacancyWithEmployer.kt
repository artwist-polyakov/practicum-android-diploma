package ru.practicum.android.diploma.common.data.db.relations

import androidx.room.Relation
import ru.practicum.android.diploma.common.data.db.entity.EmployerEntity
import ru.practicum.android.diploma.common.data.db.entity.VacancyEntity

data class VacancyWithEmployer(
    val vacancyId: Int,
    val employerId: Int,
    @Relation(parentColumn = "vacancyId", entity = VacancyEntity::class, entityColumn = "id")
    val vacancy: VacancyEntity,
    @Relation(parentColumn = "employerId", entity = EmployerEntity::class, entityColumn = "id")
    val employer: EmployerEntity,
)
