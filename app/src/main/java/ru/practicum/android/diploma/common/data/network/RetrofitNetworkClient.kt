package ru.practicum.android.diploma.common.data.network

import android.content.Context
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import ru.practicum.android.diploma.common.data.dto.Response
import ru.practicum.android.diploma.common.data.network.requests.AreasRequest
import ru.practicum.android.diploma.common.data.network.requests.IndustriesRequest
import ru.practicum.android.diploma.common.data.network.requests.SingleVacancyRequest
import ru.practicum.android.diploma.common.data.network.requests.VacanciesSearchRequest
import ru.practicum.android.diploma.common.data.network.response.AreaSearchResponse
import ru.practicum.android.diploma.common.data.network.response.IndustriesSearchResponse
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
                    is VacanciesSearchRequest -> makeVacancySearchRequest(dto)

                    is AreasRequest -> makeAreasRequest(dto)

                    is SingleVacancyRequest -> makeSingleVacancyRequest(dto)

                    is IndustriesRequest -> makeIndustriesRequest(dto)

                    else -> Response().apply { resultCode = 400 }
                }
                response.apply { resultCode = 200 }
            } catch (e: Throwable) {
                // todo Удалить после отладки
//                Log.e("NetworkClient", "Произошла ошибка при запросе", e)
//                e.printStackTrace()
//                if (e is retrofit2.HttpException) {
//                    val errorBody = e.response()?.errorBody()?.string()
//                    Log.e("NetworkClient", "Тело ошибки: $errorBody")
//                }
                // Удалять до сюда
                Response().apply { resultCode = 500 }
            }
        }


    }

    private suspend fun makeVacancySearchRequest(dto: VacanciesSearchRequest): Response {
        return hhService.searchVacancies(
            text = dto.text,
            page = dto.page,
            perPage = dto.perPage,
            area = dto.area,
            industry = dto.industry,
            salary = dto.salary,
            onlyWithSalary = dto.onlyWithSalary
        )
    }

    private suspend fun makeSingleVacancyRequest(dto: SingleVacancyRequest): Response {
        return SingleVacancyResponse(vacancy = hhService.getVacancy(vacancyId = dto.vacancyId)).apply {
            resultCode = 200
        }
    }

    private suspend fun makeAreasRequest(dto: AreasRequest): Response {
        return AreaSearchResponse(areas = hhService.getAreas()).apply {
            resultCode = 200
        }
    }

    private suspend fun makeIndustriesRequest(dto: IndustriesRequest): Response {
        return IndustriesSearchResponse(industries = hhService.getIndustries()).apply {
            resultCode = 200
        }
    }
}
