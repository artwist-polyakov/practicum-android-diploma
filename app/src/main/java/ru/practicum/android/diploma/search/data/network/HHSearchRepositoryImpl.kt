package ru.practicum.android.diploma.search.data.network

import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import ru.practicum.android.diploma.common.data.dto.AreasDto
import ru.practicum.android.diploma.common.data.dto.IndustriesDto
import ru.practicum.android.diploma.common.data.dto.Resource
import ru.practicum.android.diploma.common.data.dto.Response
import ru.practicum.android.diploma.common.data.dto.VacancyItemDto
import ru.practicum.android.diploma.common.data.network.NetworkClient
import ru.practicum.android.diploma.common.data.network.requests.AreasRequest
import ru.practicum.android.diploma.common.data.network.requests.SingleVacancyRequest
import ru.practicum.android.diploma.common.data.network.requests.VacanciesSearchRequest
import ru.practicum.android.diploma.common.data.network.response.AreaSearchResponse
import ru.practicum.android.diploma.common.data.network.response.HHSearchResponse
import ru.practicum.android.diploma.common.data.network.response.IndustriesSearchResponse
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
    ): Flow<Resource<HHSearchResponse>> = flow {
        val request = VacanciesSearchRequest(
            text = query,
            page = page,
            perPage = perPage,
            salary = salary,
            onlyWithSalary = onlyWithSalary
        )
        handleResponse(HHSearchResponse::class ,networkClient.doRequest(request))
    }

    override fun getVacancy(id: Int): Flow<Resource<VacancyItemDto>> = flow {
        val request = SingleVacancyRequest(vacancyId = id)
        handleResponse(VacancyItemDto::class, networkClient.doRequest(request))
    }

    override fun getAreas(): Flow<Resource<List<AreasDto>>> = flow {
        val request = AreasRequest()
        handleResponse(AreaSearchResponse::class, networkClient.doRequest(request))
    }

    override fun getIndustries(): Flow<Resource<List<IndustriesDto>?>> = flow {
        val request = AreasRequest()
        handleResponse(IndustriesSearchResponse::class, networkClient.doRequest(request))
    }

    private fun <T>handleResponse(type: T, request: Response): Flow<Resource<T>> = flow {
        val response = networkClient.doRequest(request)
        when (response.resultCode) {
            -1 -> emit(Resource.Error(NetworkErrors.NoInternet))
            200 -> with(response as T) {
                emit(Resource.Success(this))
            }
            400 -> emit(Resource.Error(NetworkErrors.ClientError))
            500 -> emit(Resource.Error(NetworkErrors.ServerError))
            else -> emit(Resource.Error(NetworkErrors.UnknownError))
        }
    }
}
