package ru.practicum.android.diploma.common.data.db.dao

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Query
import androidx.room.Upsert

/*
@Dao
interface FavoriteVacancyDAO {
    @Upsert(entity = VacancyEntity::class, onConflict = OnConflictStrategy.REPLACE)
    fun insertVacancy(vacancy: VacancyEntity)

    @Delete(entity = VacancyEntity::class)
    fun deleteVacancy(vacancy: VacancyEntity)

    @Query("SELECT * FROM favourite_vacancy_table ORDER BY addTime DESC")
    fun getAllVacancies(): List<VacancyEntity>

    @Query("SELECT * FROM favourite_vacancy_table WHERE id = :id")
    fun getVacancyById(id: Int): VacancyEntity
}
*/
