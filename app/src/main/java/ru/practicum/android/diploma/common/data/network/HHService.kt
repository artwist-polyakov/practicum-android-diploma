package ru.practicum.android.diploma.common.data.network

import retrofit2.http.GET
import retrofit2.http.Headers
import retrofit2.http.Path
import retrofit2.http.Query
import ru.practicum.android.diploma.BuildConfig
import ru.practicum.android.diploma.common.data.dto.AreasDto
import ru.practicum.android.diploma.common.data.dto.IndustriesDto
import ru.practicum.android.diploma.common.data.dto.VacancyItemDto
import ru.practicum.android.diploma.common.data.network.response.HHSearchResponse

// todo реализовать передачу токена
interface HHService {

    @Headers("Authorization: Bearer ${BuildConfig.HH_ACCESS_TOKEN}",
        "HH-User-Agent: Practicum HH Client/1.0 (master@artwist.ru)")
    @GET("vacancies")
    suspend fun searchVacancies(
        @Query("text") text: String = "",
        @Query("page") page: Int = 0,
        @Query("per_page") perPage: Int = 20,
        @Query("area") area: Int? = null,
        @Query("search_field") searchField: String? = "name",
        @Query("industry") industry: Int? = null,
        @Query("salary") salary: Int? = null,
        @Query("only_with_salary") onlyWithSalary: Boolean = false
    ): HHSearchResponse

    @Headers("Authorization: Bearer ${BuildConfig.HH_ACCESS_TOKEN}",
        "HH-User-Agent: Practicum HH Client/1.0 (master@artwist.ru)")
    @GET("vacancies/{vacancy_id}")
    suspend fun getVacancy(
        @Path("vacancy_id") vacancyId: Int
    ): VacancyItemDto

    @GET("areas")
    suspend fun getAreas(): List<AreasDto>

    @GET("industries")
    suspend fun getIndustries(): List<IndustriesDto>
}
