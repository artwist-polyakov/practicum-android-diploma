package ru.practicum.android.diploma.search.data.network

import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import ru.practicum.android.diploma.common.data.dto.IndustriesDto
import ru.practicum.android.diploma.common.data.dto.Resource
import ru.practicum.android.diploma.common.data.dto.Response
import ru.practicum.android.diploma.common.data.network.NetworkClient
import ru.practicum.android.diploma.common.data.network.requests.AreasRequest
import ru.practicum.android.diploma.common.data.network.requests.SingleVacancyRequest
import ru.practicum.android.diploma.common.data.network.requests.VacanciesSearchRequest
import ru.practicum.android.diploma.common.data.network.response.AreaSearchResponse
import ru.practicum.android.diploma.common.data.network.response.HHSearchResponse
import ru.practicum.android.diploma.common.data.network.response.IndustriesSearchResponse
import ru.practicum.android.diploma.common.data.network.response.SingleVacancyResponse
import ru.practicum.android.diploma.common.domain.models.NetworkErrors

class HHSearchRepositoryImpl(
    private val networkClient: NetworkClient
) : HHSearchRepository {
    override fun getVacancies(
        query: String,
        page: Int,
        perPage: Int,
        salary: Int?,
        onlyWithSalary: Boolean
    ) : Flow<Resource<HHSearchResponse>> {
        val request = VacanciesSearchRequest(
            text = query,
            page = page,
            perPage = perPage,
            salary = salary,
            onlyWithSalary = onlyWithSalary
        )
        return handleResponse<HHSearchResponse> { networkClient.doRequest(request) }
    }

    override fun getVacancy(id: Int) : Flow<Resource<SingleVacancyResponse>> {
        val request = SingleVacancyRequest(vacancyId = id)
        return handleResponse<SingleVacancyResponse> { networkClient.doRequest(request) }
    }

    override fun getAreas() : Flow<Resource<AreaSearchResponse>> {
        val request = AreasRequest()
        return handleResponse<AreaSearchResponse> { networkClient.doRequest(request) }
    }

    override fun getIndustries() : Flow<Resource<IndustriesSearchResponse>> {
        val request = AreasRequest()
        return handleResponse<IndustriesSearchResponse> { networkClient.doRequest(request) }
    }

    private inline fun <reified T : Any> handleResponse(
        crossinline functionToHandle: suspend () -> Response
    ): Flow<Resource<T>> = flow {
        try {
            val response = functionToHandle()
            when (response.resultCode) {
                -1 -> emit(Resource.Error(NetworkErrors.NoInternet))
                200 -> {
                    val data = response as? T ?: throw ClassCastException("Невозможно преобразовать результат к ${T::class}")
                    emit(Resource.Success(data))
                }

                400 -> emit(Resource.Error(NetworkErrors.ClientError))
                500 -> emit(Resource.Error(NetworkErrors.ServerError))
                else -> emit(Resource.Error(NetworkErrors.UnknownError))
            }
        } catch (e: Exception) {
            emit(Resource.Error(NetworkErrors.UnknownError))
        }
    }
}
