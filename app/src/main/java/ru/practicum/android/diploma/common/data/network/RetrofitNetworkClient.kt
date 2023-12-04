package ru.practicum.android.diploma.common.data.network

import android.content.Context
import android.util.Log
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import ru.practicum.android.diploma.common.data.dto.Response
import ru.practicum.android.diploma.common.data.network.requests.AreasRequest
import ru.practicum.android.diploma.common.data.network.requests.SingleVacancyRequest
import ru.practicum.android.diploma.common.data.network.requests.VacanciesSearchRequest
import ru.practicum.android.diploma.common.data.network.response.AreaSearchResponse
import ru.practicum.android.diploma.common.data.network.response.SingleVacancyResponse
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

                    is AreasRequest -> {
                        AreaSearchResponse(areas = hhService.getAreas()).apply {
                            resultCode = 200
                        }
                    }

                    is SingleVacancyRequest -> {
                        SingleVacancyResponse(vacancy = hhService.getVacancy(vacancyId = dto.vacancyId)).apply {
                            resultCode = 200
                        }
                    }

                    else -> Response().apply { resultCode = 400 }
                }
                response.apply { resultCode = 200 }
            } catch (e: Throwable) {
                // TODO Удалить после отладки
                Log.e("NetworkClient", "Произошла ошибка при запросе", e)
                e.printStackTrace()
                if (e is retrofit2.HttpException) {
                    val errorBody = e.response()?.errorBody()?.string()
                    Log.e("NetworkClient", "Тело ошибки: $errorBody")
                }
                // Удалять до сюда
                Response().apply { resultCode = 500 }
            }
        }
    }
}
