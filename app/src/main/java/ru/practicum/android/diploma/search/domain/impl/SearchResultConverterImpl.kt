package ru.practicum.android.diploma.search.domain.impl

import ru.practicum.android.diploma.common.data.dto.AreasDto
import ru.practicum.android.diploma.common.data.dto.IndustriesDto
import ru.practicum.android.diploma.common.data.dto.Resource
import ru.practicum.android.diploma.common.data.dto.VacancyItemDto
import ru.practicum.android.diploma.common.data.network.response.AreaSearchResponse
import ru.practicum.android.diploma.common.data.network.response.HHSearchResponse
import ru.practicum.android.diploma.common.data.network.response.IndustriesSearchResponse
import ru.practicum.android.diploma.common.domain.models.NetworkErrors
import ru.practicum.android.diploma.search.domain.api.SearchResultConverter
import ru.practicum.android.diploma.search.domain.models.SingleTreeElement
import ru.practicum.android.diploma.search.domain.models.VacanciesSearchResult
import ru.practicum.android.diploma.search.domain.models.VacancyGeneral

class SearchResultConverterImpl : SearchResultConverter {
    override fun mapSearchResponce(from: Resource<HHSearchResponse>): Resource<VacanciesSearchResult> {
        return when (from) {
            is Resource.Success -> {
                Resource.Success(VacanciesSearchResult(
                    vacanciesFound = from.data?.found ?: 0,
                    totalPages = from.data?.pages ?: 0,
                    currentPage = from.data?.page ?: 0,
                    vacancies = from.data?.items?.map { map(it) } ?: emptyList()
                ))
            }

            is Resource.Error -> {
                Resource.Error(from.error ?: NetworkErrors.UnknownError)
            }
        }
    }

    override fun mapAreaResponse(from: Resource<AreaSearchResponse>): Resource<List<SingleTreeElement>> {
        return when (from) {
            is Resource.Success -> {
                Resource.Success(from.data?.areas?.map { map(it) } ?: emptyList())
            }

            is Resource.Error -> {
                Resource.Error(from.error ?: NetworkErrors.UnknownError)
            }
        }
    }

    override fun mapIndustriesResponse(from: Resource<IndustriesSearchResponse>): Resource<List<SingleTreeElement>> {
        return when (from) {
            is Resource.Success -> {
                Resource.Success(from.data?.industries?.map { map(it) } ?: emptyList())
            }

            is Resource.Error -> {
                Resource.Error(from.error ?: NetworkErrors.UnknownError)
            }
        }
    }

    override fun map(from: VacancyItemDto): VacancyGeneral {
        return with(from) {
            VacancyGeneral(
                id = id,
                title = name,
                region = area?.name,
                employerName = employer?.name,
                employerLogo = employer?.logoUrls?.medium,
                haveSalary = salary != null,
                salaryFrom = salary?.from,
                salaryTo = salary?.to,
                salaryCurrency = salary?.currency?.symbol
            )
        }
    }

    override fun map(from: AreasDto): SingleTreeElement {
        return with(from) {
            SingleTreeElement(
                id = id,
                name = name,
                parent = parentId?.toInt(),
                hasChildrens = areas?.isNotEmpty() ?: false,
                children = areas?.map { map(it) }
            )
        }
    }

    override fun map(from: IndustriesDto): SingleTreeElement {
        return with(from) {
            SingleTreeElement(
                id = id,
                name = name,
                parent = null,
                hasChildrens = if (!id.contains(".")) false else true,
                children = industries?.map { map(it) }
            )
        }
    }

}
