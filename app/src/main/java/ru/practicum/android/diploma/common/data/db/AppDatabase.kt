package ru.practicum.android.diploma.common.data.db

import androidx.room.Database
import androidx.room.RoomDatabase
import ru.practicum.android.diploma.common.data.db.dao.EmployerDao
import ru.practicum.android.diploma.common.data.db.dao.VacancyDao
import ru.practicum.android.diploma.common.data.db.dao.VacancyEmployerReferenceDao
import ru.practicum.android.diploma.common.data.db.entity.EmployerEntity
import ru.practicum.android.diploma.common.data.db.entity.VacancyEmployerReference
import ru.practicum.android.diploma.common.data.db.entity.VacancyEntity

@Database(
    version = 1,
    entities = [
        VacancyEntity::class,
        EmployerEntity::class,
        VacancyEmployerReference::class,
    ]
)
abstract class AppDatabase : RoomDatabase() {
    abstract fun vacancyDao(): VacancyDao
    abstract fun employerDao(): EmployerDao
    abstract fun vacancyEmployerReferenceDao(): VacancyEmployerReferenceDao
}
