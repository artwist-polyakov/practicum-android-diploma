package ru.practicum.android.diploma.common.data.db

import androidx.room.Database
import androidx.room.RoomDatabase
import ru.practicum.android.diploma.common.data.db.dao.VacancyEmployerReferenceDao
import ru.practicum.android.diploma.common.data.db.entity.EmployerEntity
import ru.practicum.android.diploma.common.data.db.entity.VacancyEmployerReference
import ru.practicum.android.diploma.common.data.db.entity.VacancyEntity

@Database(
    version = 0,
    entities = [
        VacancyEntity::class,
        EmployerEntity::class,
        VacancyEmployerReference::class,
    ]
)
abstract class Database : RoomDatabase() {
    abstract fun vacancyDao(): VacancyEmployerReferenceDao
}
