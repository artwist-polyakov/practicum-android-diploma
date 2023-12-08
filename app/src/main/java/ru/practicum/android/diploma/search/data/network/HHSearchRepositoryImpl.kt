package ru.practicum.android.diploma.search.data.network

import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import okio.IOException
import retrofit2.HttpException
import ru.practicum.android.diploma.common.domain.models.NetworkResult
import ru.practicum.android.diploma.common.data.dto.Response
import ru.practicum.android.diploma.common.data.network.NetworkClient
import ru.practicum.android.diploma.common.data.network.requests.AreasRequest
import ru.practicum.android.diploma.common.data.network.requests.IndustriesRequest
import ru.practicum.android.diploma.common.data.network.requests.SimilarVacanciesRequest
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
        query: String?,
        page: Int,
        perPage: Int,
        salary: Int?,
        area: Int?,
        industry: String?,
        onlyWithSalary: Boolean
    ): Flow<NetworkResult<HHSearchResponse>> {
        val request = VacanciesSearchRequest(
            text = query,
            page = page,
            perPage = perPage,
            salary = salary,
            area = area,
            onlyWithSalary = onlyWithSalary
        )
        return handleResponse<HHSearchResponse> { networkClient.doRequest(request) }
    }

    override fun getVacancy(id: Int): Flow<NetworkResult<SingleVacancyResponse>> {
        val request = SingleVacancyRequest(vacancyId = id)
        return handleResponse<SingleVacancyResponse> { networkClient.doRequest(request) }
    }

    override fun getSimilarVacancies(
        id: Int,
        page: Int,
        perPage: Int
    ): Flow<NetworkResult<HHSearchResponse>> {
        val request = SimilarVacanciesRequest(
            vacancyId = id,
            page = page,
            perPage = perPage
        )
        return handleResponse<HHSearchResponse> { networkClient.doRequest(request) }
    }

    override fun getAreas(forId: Int?): Flow<NetworkResult<AreaSearchResponse>> {
        val request = AreasRequest()
        return handleResponse<AreaSearchResponse> { networkClient.doRequest(request) }
    }

    override fun getIndustries(forId: Int?): Flow<NetworkResult<IndustriesSearchResponse>> {
        val request = IndustriesRequest()
        return handleResponse<IndustriesSearchResponse> { networkClient.doRequest(request) }
    }

    @Suppress("TooGenericExceptionCaught")
    private inline fun <reified T> handleResponse(
        crossinline functionToHandle: suspend () -> Response
    ): Flow<NetworkResult<T>> = flow {
        try {
            val response = functionToHandle()
            when (response.resultCode) {
                NO_INTERNET_ERROR -> emit(NetworkResult.Error(NetworkErrors.NoInternet))
                SUCCESS -> {
                    val data =
                        response as? T ?: throw ClassCastException("Невозможно преобразовать результат к ${T::class}")
                    emit(NetworkResult.Success(data))
                }

                CLIENT_ERROR -> emit(NetworkResult.Error(NetworkErrors.ClientError))
                SERVER_ERROR -> emit(NetworkResult.Error(NetworkErrors.ServerError))
                else -> emit(NetworkResult.Error(NetworkErrors.UnknownError))
            }
        } catch (e: IOException) {
            e.printStackTrace()
            emit(NetworkResult.Error(NetworkErrors.NoInternet))
        } catch (e: HttpException) {
            e.printStackTrace()
            emit(NetworkResult.Error(NetworkErrors.ClientError))
        } catch (e: RuntimeException) {
            e.printStackTrace()
            emit(NetworkResult.Error(NetworkErrors.ClientError))
        } catch (e: Exception) {
            e.printStackTrace()
            emit(NetworkResult.Error(NetworkErrors.ServerError))
        }
    }

    companion object {
        private const val CLIENT_ERROR = 400
        private const val SERVER_ERROR = 500
        private const val NO_INTERNET_ERROR = -1
        private const val SUCCESS = 200
    }
}
