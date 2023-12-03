package ru.practicum.android.diploma.common.data.network

import android.content.Context
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import ru.practicum.android.diploma.common.data.dto.Response
import ru.practicum.android.diploma.common.data.dto.VacanciesSearchRequest
import ru.practicum.android.diploma.common.utils.checkInternetReachability

class RetrofitNetworkClient(
    private val hhService: HHService,
    private val context: Context
) : NetworkClient {
    override suspend fun doRequest(dto: Any): Response {
        if (!context.checkInternetReachability()) return Response().apply { resultCode = -1 }
        return withContext(Dispatchers.IO) {
            try {
                val response = when (dto) {
                    is VacanciesSearchRequest -> hhService.searchVacancies(
                        text = dto.text,
                        page = dto.page,
                        perPage = dto.perPage,
                        area = dto.area,
                        industry = dto.industry,
                        salary = dto.salary,
                        onlyWithSalary = dto.onlyWithSalary
                        )
                    else -> Response().apply { resultCode = 400 }
                }
                response.apply { resultCode = 200 }
            } catch (e: Throwable) {
                Response().apply { resultCode = 500 }
            }
        }
    }
}
