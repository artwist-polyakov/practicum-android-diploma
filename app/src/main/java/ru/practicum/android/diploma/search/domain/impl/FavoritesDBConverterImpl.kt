package ru.practicum.android.diploma.search.domain.impl

import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import ru.practicum.android.diploma.common.data.dto.VacancyWithEmployerDTO
import ru.practicum.android.diploma.search.domain.api.FavoritesDBConverter
import ru.practicum.android.diploma.search.domain.models.VacanciesSearchResult
import ru.practicum.android.diploma.search.domain.models.VacancyGeneral

class FavoritesDBConverterImpl(
    val gsonService: Gson
) : FavoritesDBConverter {

    override fun map(
        from: List<VacancyWithEmployerDTO>,
        page: Int,
        found: Int,
        totalPages: Int
    ): VacanciesSearchResult =
        with(from) {
            VacanciesSearchResult(
                vacanciesFound = found,
                totalPages = totalPages,
                currentPage = page,
                vacancies = map { mapDtoToGeneral(it) }
            )
        }

    private fun mapDtoToGeneral(from: VacancyWithEmployerDTO): VacancyGeneral {
        val type = object : TypeToken<Map<String, String>>() {}.type
        val logoMap: Map<String, String> = gsonService.fromJson(from.logosJSON, type)
        return with(from) {
            VacancyGeneral(
                id = vacancyId,
                title = title,
                employerName = employerName,
                employerLogo = logoMap.getOrDefault("medium", null),
                haveSalary = salaryFrom != null || salaryTo != null,
                salaryFrom = salaryFrom,
                salaryTo = salaryTo,
                salaryCurrency = currency,
            )
        }
    }
}
