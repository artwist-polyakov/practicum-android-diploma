package ru.practicum.android.diploma.common.data.db.dao

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import ru.practicum.android.diploma.common.data.db.entity.EmployerEntity

@Dao
interface EmployerDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun addEmployer(data: EmployerEntity)

    @Delete
    suspend fun removeEmployer(data: EmployerEntity): Int

    @Query("SELECT * FROM employers WHERE id = :id")
    suspend fun getEmployer(id: Int): EmployerEntity?
}
