package ru.practicum.android.diploma.common.data.db.dao

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
/*
@Dao
interface FavoutiteVacancyDAO {
    @Upsert(entity = VacancyEntity::class, onConflict = OnConflictStrategy.REPLACE)
    fun insert (vacancy: VacancyEntity)

    @Delete(entity = VacancyEntity::class)
    fun delete (track:VacancyEntity)

    @Query("SELECT * FROM favourite_vacancy_table ORDER BY addTime DESC")
    fun query():List<VacancyEntity>

    @Query("SELECT * FROM favourite_vacancy_table WHERE trackId=:vacancyId")
    fun queryId(searchId:Long):VacancyEntity
}
*/
