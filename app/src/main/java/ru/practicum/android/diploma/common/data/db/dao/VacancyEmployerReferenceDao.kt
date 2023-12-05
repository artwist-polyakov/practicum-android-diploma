package ru.practicum.android.diploma.common.data.db.dao

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Transaction
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.count
import ru.practicum.android.diploma.common.data.db.entity.VacancyEmployerReference
import ru.practicum.android.diploma.common.data.db.relations.VacancyWithEmployer

@Dao
interface VacancyEmployerReferenceDao : VacancyDao, EmployerDao {
    @Insert
    suspend fun addReference(data: VacancyEmployerReference)

    @Delete
    suspend fun removeReference(data: VacancyEmployerReference): Int

    @Query("SELECT * FROM vacancy_employer_reference")
    fun getVacancies(): Flow<List<VacancyEmployerReference>>

    @Query("SELECT * FROM vacancy_employer_reference WHERE employerId = :employerId")
    fun getVacancies(employerId: Int): Flow<List<VacancyEmployerReference>>

    @Query("SELECT * FROM vacancy_employer_reference WHERE vacancyId = :vacancyId")
    fun getVacancy(vacancyId: Int): Flow<VacancyEmployerReference?>

    @Transaction
    suspend fun addVacancy(data: VacancyWithEmployer) {
        addVacancy(data.vacancy)
        addEmployer(data.employer)
        addReference(
            VacancyEmployerReference(
                vacancyId = data.vacancy.id,
                employerId = data.employer.id
            )
        )
    }

    @Transaction
    suspend fun removeVacancy(data: VacancyWithEmployer) {
        removeVacancy(data.vacancy)
        removeReference(
            VacancyEmployerReference(
                vacancyId = data.vacancy.id,
                employerId = data.employer.id
            )
        )
        getVacancies(data.employer.id).collect{
            if (it.isEmpty()){
                removeEmployer(data.employer)
            }
        }
    }
}
