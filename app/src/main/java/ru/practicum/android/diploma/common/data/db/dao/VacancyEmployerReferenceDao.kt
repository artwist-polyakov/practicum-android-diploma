package ru.practicum.android.diploma.common.data.db.dao

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Transaction
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.first
import ru.practicum.android.diploma.common.data.db.entity.EmployerEntity
import ru.practicum.android.diploma.common.data.db.entity.VacancyEmployerReference
import ru.practicum.android.diploma.common.data.db.entity.VacancyEntity
import ru.practicum.android.diploma.common.data.dto.VacancyWithEmployerDTO

@Dao
interface VacancyEmployerReferenceDao : VacancyDao, EmployerDao {
    @Insert
    suspend fun addReference(data: VacancyEmployerReference)

    @Delete
    suspend fun removeReference(data: VacancyEmployerReference)

    @Transaction
    @Query(
        """
         SELECT *
         FROM vacancies
         LEFT JOIN vacancy_employer_reference 
         ON vacancies.id = vacancy_employer_reference.vacancyId
         LEFT JOIN employers ON vacancy_employer_reference.employerId = employers.id
     """
    )
    fun getVacanciesWithEmployer(): Flow<List<VacancyWithEmployerDTO>>

    @Transaction
    @Query(
        """
         SELECT * 
         FROM vacancies
         LEFT JOIN vacancy_employer_reference
         ON vacancies.id = vacancy_employer_reference.vacancyId
         LEFT JOIN employers 
         ON vacancy_employer_reference.employerId = employers.id
         ORDER BY vacancies.lastUpdate DESC"""
    )
    fun getVacancies(): Flow<List<VacancyEmployerReference>>

    @Transaction
    @Query(
        """
         SELECT *
         FROM (SELECT * FROM vacancy_employer_reference WHERE employerId = :employerId) as reference
         LEFT JOIN vacancies
         ON reference.vacancyId = vacancies.id
         LEFT JOIN employers 
         ON reference.employerId = employers.id
         ORDER BY vacancies.lastUpdate DESC
         LIMIT :limit OFFSET :page * :limit"""
    )
    fun getVacancies(employerId: Int, page: Int = 0, limit: Int = DEFAULT_LIMIT): Flow<List<VacancyWithEmployerDTO>>

    @Query(
        """
         SELECT *
         FROM (SELECT * FROM vacancies WHERE id = :vacancyId) as vac
         LEFT JOIN vacancy_employer_reference
         ON vac.id = vacancy_employer_reference.vacancyId
         LEFT JOIN employers 
         ON vacancy_employer_reference.employerId = employers.id
         WHERE vac.id = :vacancyId
         ORDER BY vac.lastUpdate DESC"""
    )
    fun getVacancy(vacancyId: Int): Flow<VacancyWithEmployerDTO?>

    @Transaction
    suspend fun addVacancy(vacancy: VacancyEntity, employer: EmployerEntity?) {
        addVacancy(vacancy)
        employer?.let {
            addEmployer(it)
            addReference(
                VacancyEmployerReference(
                    vacancyId = vacancy.id,
                    employerId = it.id
                )
            )
        }
    }

    @Transaction
    suspend fun removeVacancy(vacancy: VacancyEntity, employer: EmployerEntity?) {
        removeVacancy(vacancy)
        employer?.let {
            if (getVacancies(it.id).first().isEmpty()) {
                removeEmployer(it.id)
            }
        }
    }

    private suspend fun removeEmployer(id: Int) {
        getEmployer(id)?.let { employer ->
            employer?.first()?.let { toDelete ->
                removeEmployer(toDelete)
            }
        }
    }

    companion object {
        private const val DEFAULT_LIMIT = 20
    }
}
