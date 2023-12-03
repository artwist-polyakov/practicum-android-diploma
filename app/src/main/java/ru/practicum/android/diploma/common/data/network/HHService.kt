package ru.practicum.android.diploma.common.data.network

import retrofit2.http.GET
import retrofit2.http.Query
import ru.practicum.android.diploma.common.data.dto.HHSearchResponce

interface HHService {

    @GET("vacancies")
    suspend fun searchVacancies(
        @Query("text") text: String,
        @Query("page") page: Int = 0,
        @Query("per_page") perPage: Int,
    ): HHSearchResponce

}
