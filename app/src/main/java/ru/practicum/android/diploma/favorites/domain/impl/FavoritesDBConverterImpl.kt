package ru.practicum.android.diploma.favorites.domain.impl

import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import ru.practicum.android.diploma.common.data.dto.ContactsDto
import ru.practicum.android.diploma.common.data.dto.PhoneDto
import ru.practicum.android.diploma.common.data.dto.VacancyWithEmployerDTO
import ru.practicum.android.diploma.favorites.domain.api.FavoritesDBConverter
import ru.practicum.android.diploma.search.domain.models.VacanciesSearchResult
import ru.practicum.android.diploma.search.domain.models.VacancyGeneral
import ru.practicum.android.diploma.vacancy.domain.models.Contacts
import ru.practicum.android.diploma.vacancy.domain.models.DetailedVacancyItem

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

    override fun map(from: VacancyWithEmployerDTO): DetailedVacancyItem {
        val type = object : TypeToken<Map<String, String>>() {}.type
        val logoMap: Map<String, String>? = gsonService.fromJson(from.logosJSON, type)
        val typePhones = object : TypeToken<ContactsDto>() {}.type
        val phones: ContactsDto? = gsonService.fromJson(from.phonesJSON, typePhones)
        val contact: Contacts?
        var listOfPhones: List<Pair<String, String>>? = null
        if (phones != null) {
            listOfPhones = phones.phones?.map { phoneDtoToPhone(it) }
        }
        contact = Contacts(
            name = from.contactName,
            email = from.contactEmail,
            phones = listOfPhones
        )
        return with(from) {
            DetailedVacancyItem(
                id = vacancyId,
                title = title,
                employerName = employerName,
                employerLogo = logoMap?.getOrDefault(LOGO_SIZE, null),
                area = city,
                haveSalary = salaryFrom != null || salaryTo != null,
                salaryFrom = salaryFrom,
                salaryTo = salaryTo,
                salaryCurrency = currency,
                experience = experience,
                employment = employment,
                schedule = schedule,
                description = jobDescription,
                keySkills = gsonService.fromJson(keySkillsJSON, Array<String>::class.java).toList(),
                contacts = contact,
                favorite = true
            )
        }
    }

    private fun mapDtoToGeneral(from: VacancyWithEmployerDTO): VacancyGeneral {
        val type = object : TypeToken<Map<String, String>>() {}.type
        val logoMap: Map<String, String>? = gsonService.fromJson(from.logosJSON, type)
        return with(from) {
            VacancyGeneral(
                id = vacancyId,
                region = city,
                title = title,
                employerName = employerName,
                employerLogo = logoMap?.getOrDefault(LOGO_SIZE, null),
                haveSalary = salaryFrom != null || salaryTo != null,
                salaryFrom = salaryFrom,
                salaryTo = salaryTo,
                salaryCurrency = currency,
            )
        }
    }

    private fun phoneDtoToPhone(from: PhoneDto): Pair<String, String> {
        return with(from) {
            Pair(
                from.comment ?: "",
                (from.country ?: "").toString() +
                    " " + (from.city ?: "").toString() +
                    " " + (from.number ?: "").toString()
            )
        }
    }

    companion object {
        const val LOGO_SIZE = "240"
    }
}
