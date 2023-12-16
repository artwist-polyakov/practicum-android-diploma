package ru.practicum.android.diploma.common.data.network

import android.content.Context
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import retrofit2.HttpException
import ru.practicum.android.diploma.common.data.dto.Response
import ru.practicum.android.diploma.common.data.network.requests.AreasRequest
import ru.practicum.android.diploma.common.data.network.requests.IndustriesRequest
import ru.practicum.android.diploma.common.data.network.requests.SimilarVacanciesRequest
import ru.practicum.android.diploma.common.data.network.requests.SingleVacancyRequest
import ru.practicum.android.diploma.common.data.network.requests.VacanciesSearchRequest
import ru.practicum.android.diploma.common.data.network.response.AreaSearchResponse
import ru.practicum.android.diploma.common.data.network.response.IndustriesSearchResponse
import ru.practicum.android.diploma.common.data.network.response.SingleVacancyResponse
import ru.practicum.android.diploma.common.utils.checkInternetReachability
import java.io.IOException

class RetrofitNetworkClient(
    private val hhService: HHService,
    private val context: Context
) : NetworkClient {
    @Suppress("TooGenericExceptionCaught")
    override suspend fun doRequest(dto: Any): Response {
        if (!context.checkInternetReachability()) return Response().apply { resultCode = NO_INTERNET_ERROR }
        return withContext(Dispatchers.IO) {
            try {
                doRequestInternal(dto)
            } catch (e: IOException) {
                e.printStackTrace()
                Response().apply { resultCode = NO_INTERNET_ERROR }
            } catch (e: HttpException) {
                e.printStackTrace()
                getHttpExceptionResponse()
            } catch (e: RuntimeException) {
                e.printStackTrace()
                getRuntimeExceptionResponse()
            } catch (e: Exception) {
                e.printStackTrace()
                Response().apply { resultCode = SERVER_ERROR }
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
        ).apply {
            resultCode = SUCCESS
        }
    }

    private suspend fun makeSingleVacancyRequest(dto: SingleVacancyRequest): Response {
        return SingleVacancyResponse(vacancy = hhService.getVacancy(vacancyId = dto.vacancyId)).apply {
            resultCode = SUCCESS
        }
    }

    private suspend fun makeSimilarVacanciesRequest(dto: SimilarVacanciesRequest): Response {
        return hhService.searchSimilarVacancies(
            vacancyId = dto.vacancyId,
            page = dto.page,
            perPage = dto.perPage
        ).apply {
            resultCode = SUCCESS
        }
    }

    private suspend fun makeAreasRequest(dto: AreasRequest): Response {
        dto.id?.let {
            return AreaSearchResponse(areas = listOf(hhService.getAreaById(areaId = it))).apply {
                resultCode = SUCCESS
            }
        }
        return AreaSearchResponse(areas = hhService.getAreas()).apply {
            resultCode = SUCCESS
        }
    }

    private suspend fun makeIndustriesRequest(dto: IndustriesRequest): Response {
        return IndustriesSearchResponse(industries = hhService.getIndustries()).apply {
            resultCode = SUCCESS
        }
    }

    private suspend fun doRequestInternal(dto: Any): Response {
        return when (dto) {
            is VacanciesSearchRequest -> return makeVacancySearchRequest(dto)

            is AreasRequest -> makeAreasRequest(dto)

            is SingleVacancyRequest -> makeSingleVacancyRequest(dto)

            is IndustriesRequest -> makeIndustriesRequest(dto)

            is SimilarVacanciesRequest -> makeSimilarVacanciesRequest(dto)

            else -> Response().apply { resultCode = CLIENT_ERROR }
        }
    }

    private suspend fun getHttpExceptionResponse(): Response {
        return Response().apply { resultCode = CLIENT_ERROR }
    }

    private suspend fun getRuntimeExceptionResponse(): Response {
        return Response().apply { resultCode = CLIENT_ERROR }
    }

    companion object {
        private const val CLIENT_ERROR = 400
        private const val SERVER_ERROR = 500
        private const val NO_INTERNET_ERROR = -1
        private const val SUCCESS = 200
    }
}
