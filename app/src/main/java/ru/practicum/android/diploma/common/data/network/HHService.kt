package ru.practicum.android.diploma.common.data.network

import retrofit2.http.GET
import retrofit2.http.Header
import retrofit2.http.Path
import retrofit2.http.Query
import ru.practicum.android.diploma.common.data.dto.AreasDto
import ru.practicum.android.diploma.common.data.dto.IndustriesDto
import ru.practicum.android.diploma.common.data.dto.Response
import ru.practicum.android.diploma.common.data.dto.VacancyItemDto
import ru.practicum.android.diploma.common.data.network.response.HHSearchResponse

// todo реализовать передачу токена
interface HHService {

    @GET("vacancies")
    suspend fun searchVacancies(
        @Header("Authorization") token: String? = null,
        @Query("text") text: String = "",
        @Query("page") page: Int = 0,
        @Query("per_page") perPage: Int = 20,
        @Query("area") area: Int? = null,
        @Query("search_field") searchField: String? = "name",
        @Query("industry") industry: Int? = null,
        @Query("salary") salary: Int? = null,
        @Query("only_with_salary") onlyWithSalary: Boolean = false
    ): HHSearchResponse

    @GET("vacancies/{vacancy_id}")
    suspend fun getVacancy(
        @Header("Authorization") token: String? = null,
        @Path("vacancy_id") vacancyId: Int
    ): VacancyItemDto

    // todo реализовать классы запроса и ответа, когда будет апи
    @GET("vacancies/{vacancy_id}/show_contacts")
    suspend fun getVacancyContacts(
        @Header("Authorization") token: String? = null,
        @Path("vacancy_id") vacancyId: Int
    ): Response

    @GET("areas")
    suspend fun getAreas(
        @Header("Authorization") token: String? = null,
    ): List<AreasDto>

    @GET("industries")
    suspend fun getIndustries(
        @Header("Authorization") token: String? = null,
    ): List<IndustriesDto>
}
