package ru.practicum.android.diploma.common.data.db.dao

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Transaction
import ru.practicum.android.diploma.common.data.db.entity.VacancyEmployerReference
import ru.practicum.android.diploma.common.data.db.relations.VacancyWithEmployer

@Dao
interface VacancyEmployerReferenceDao : VacancyDao, EmployerDao {
    @Insert
    suspend fun addReference(data: VacancyEmployerReference)

    @Delete
    suspend fun removeReference(data: VacancyEmployerReference)

    @Query("SELECT * FROM vacancy_employer_reference")
    suspend fun getVacanciesWithEmployer(): List<VacancyWithEmployer>

    @Query("SELECT * FROM vacancies")
    suspend fun getVacancies(): List<VacancyWithEmployer>

    @Query("SELECT * FROM vacancy_employer_reference WHERE employerId = :employerId")
    suspend fun getVacancies(employerId: Int): List<VacancyWithEmployer>

    @Query("SELECT * FROM vacancy_employer_reference WHERE vacancyId = :vacancyId")
    suspend fun getVacancy(vacancyId: Int): VacancyWithEmployer?

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
        if (getVacancies(data.employer.id).isEmpty())
            removeEmployer(data.employer)
    }
}
