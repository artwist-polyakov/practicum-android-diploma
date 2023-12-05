package ru.practicum.android.diploma.common.data.db.dao

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Upsert
import ru.practicum.android.diploma.common.data.db.entity.EmployerEntity
import ru.practicum.android.diploma.common.data.db.entity.VacancyEntity
import ru.practicum.android.diploma.common.domain.models.Vacancy
import ru.practicum.android.diploma.common.domain.models.VacancyForFavoriteList


@Dao
interface FavoriteVacancyDAO {
    @Upsert(entity = VacancyEntity::class)
    fun insertVacancy(vacancy: VacancyEntity, employer: EmployerEntity)

    @Delete(entity = VacancyEntity::class)
    fun deleteVacancy(vacancy: VacancyEntity)

    @Query(
        """
        SELECT v.id as vacancyId,
               e.employerName,
               e.employerLogoUrls90,
               v.city,
               v.jobName,
               v.currency,
               v.min,
               v.max
        FROM favourite_vacancy_table as v
        INNER JOIN employer_vacancy_reference as r ON v.id = r.vacancyId
        INNER JOIN employer_vacancy_table as e ON r.employerId = e.id
        """
    )
    suspend fun getAllVacanciesForFavoriteList(): List<VacancyForFavoriteList>

    @Query(
        """
        SELECT v.id,
               v.city,
               v.jobDescription,
               v.jobTiming,
               v.experience,
               v.keySkills,
               v.professionalRoles,
               v.jobName,
               v.currency,
               v.min,
               v.max,
               v.schedule,
               e.id,
               e.employerEmail,
               e.employerContactName,
               e.employerPhone,
               e.comment,
               e.employerLogoUrls90,
               e.employerLogoUrls240,
               e.employerLogoUrlsOriginal,
               e.employerName
        FROM favourite_vacancy_table as v
        INNER JOIN employer_vacancy_reference as r ON v.id = r.vacancyId
        INNER JOIN employer_vacancy_table as e ON r.employerId = e.id
        WHERE v.id = :vacancyId
        """
    )
    suspend fun getVacancyById(vacancyId: Int): Vacancy?
}


