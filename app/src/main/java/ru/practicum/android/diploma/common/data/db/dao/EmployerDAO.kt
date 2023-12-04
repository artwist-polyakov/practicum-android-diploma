package ru.practicum.android.diploma.common.data.db.dao

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Index
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Upsert

@Dao
interface EmployerDAO {
    @Upsert(entity = EmployerEntity::class, onConflict = OnConflictStrategy.REPLACE)
    fun insertEmployer(employer: EmployerEntity)

/*    @Delete(entity = EmployerEntity::class)
    fun deleteEmployer(employer: EmployerEntity)

    @Query(
        "SELECT * FROM employer_vacancy_table" ORDER BY addTime Index.Order.DESC")
        fun getAllEmployers(): List<EmployerEntity>

        @Query("SELECT * FROM employer_vacancy_table WHERE id = :id")
        fun getEmployerById(id: Int): EmployerEntity
}
*/
