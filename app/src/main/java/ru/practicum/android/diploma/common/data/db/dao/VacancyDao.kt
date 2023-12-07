package ru.practicum.android.diploma.common.data.db.dao

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Query
import androidx.room.Upsert
import ru.practicum.android.diploma.common.data.db.entity.VacancyEntity

@Dao
interface VacancyDao {
    @Upsert
    suspend fun addVacancy(data: VacancyEntity)

    @Delete
    suspend fun removeVacancy(data: VacancyEntity)

    @Query("SELECT COUNT(*) FROM vacancies")
    fun getVacanciesCount(): Int

    @Query("SELECT count(*)>0 FROM vacancies WHERE id = :id")
    fun isVacancyExists(id: Int): Boolean
}
