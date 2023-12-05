package ru.practicum.android.diploma.common.data.db.dao

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Query
import androidx.room.Upsert
import kotlinx.coroutines.flow.Flow
import ru.practicum.android.diploma.common.data.db.entity.EmployerEntity

@Dao
interface EmployerDao {
    @Upsert
    suspend fun addEmployer(data: EmployerEntity)

    @Delete
    suspend fun removeEmployer(data: EmployerEntity)

    @Query("SELECT * FROM employers WHERE id = :id")
    fun getEmployer(id: Int): Flow<EmployerEntity?>
}

