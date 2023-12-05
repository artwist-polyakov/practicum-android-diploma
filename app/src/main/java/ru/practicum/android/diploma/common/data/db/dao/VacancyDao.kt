package ru.practicum.android.diploma.common.data.db.dao

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import ru.practicum.android.diploma.common.data.db.entity.VacancyEntity

@Dao
interface VacancyDao {
    @Insert(onConflict = OnConflictStrategy.ABORT)
    suspend fun addVacancy(data: VacancyEntity)

    @Delete
    suspend fun removeVacancy(data: VacancyEntity)

    @Query("SELECT COUNT(*) > 0 FROM vacancies WHERE id = :id")
    suspend fun contains(id: Int) : Boolean
}
