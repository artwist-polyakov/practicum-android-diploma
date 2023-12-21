package ru.practicum.android.diploma.vacancy.domain.impl

import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import ru.practicum.android.diploma.common.data.db.entity.EmployerEntity
import ru.practicum.android.diploma.common.data.db.entity.VacancyEntity
import ru.practicum.android.diploma.common.data.dto.ContactsDto
import ru.practicum.android.diploma.common.data.dto.PhoneDto
import ru.practicum.android.diploma.common.data.dto.Resource
import ru.practicum.android.diploma.common.data.dto.VacancyItemDto
import ru.practicum.android.diploma.common.data.dto.VacancyWithEmployerDTO
import ru.practicum.android.diploma.common.data.network.response.SingleVacancyResponse
import ru.practicum.android.diploma.common.domain.models.NetworkErrors
import ru.practicum.android.diploma.vacancy.domain.api.SingleVacancyConverter
import ru.practicum.android.diploma.vacancy.domain.models.Contacts
import ru.practicum.android.diploma.vacancy.domain.models.DetailedVacancyItem

class SingleVacancyConverterImpl(
    private val gson: Gson
) : SingleVacancyConverter {
    override fun map(from: Resource<SingleVacancyResponse>, isFavorite: Boolean): Resource<DetailedVacancyItem> {
        return when (from) {
            is Resource.Success -> {
                from.data?.let {
                    Resource.Success(map(it.vacancy, isFavorite))
                } ?: Resource.Error(error = NetworkErrors.UnknownError)
            }

            is Resource.Error -> {
                Resource.Error(from.error ?: NetworkErrors.UnknownError)
            }
        }
    }

    override fun map(from: VacancyWithEmployerDTO, isFavorite: Boolean): Resource<DetailedVacancyItem> {
        val typeLogo = object : TypeToken<Map<String, String>>() {}.type
        val logoMap: Map<String, String>? = gson.fromJson(from.logosJSON, typeLogo)
        val keySkills: List<String>? = gson.fromJson(from.keySkillsJSON, object : TypeToken<List<String>>() {}.type)
        val phones: ContactsDto? = gson.fromJson(from.phonesJSON, object : TypeToken<ContactsDto>() {}.type)
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
            Resource.Success(
                data =
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
                    keySkills = keySkills,
                    contacts = contact,
                    favorite = isFavorite
                )
            )
        }
    }

    override fun map(from: VacancyItemDto): Pair<VacancyEntity, EmployerEntity?> {
        val logoJSON = gson.toJson(from.employer?.logoUrls)
        val keySkillsJSON = gson.toJson(from.keySkills)
        val phonesJSON = gson.toJson(from.contacts?.phones)
        return with(from) {
            Pair(
                VacancyEntity(
                    id = id,
                    title = name,
                    city = from.area?.name,
                    salaryFrom = from.salary?.from,
                    salaryTo = from.salary?.to,
                    currency = from.salary?.currency?.symbol,
                    experience = from.experience?.name,
                    employment = from.employment?.name,
                    schedule = from.schedule?.name,
                    jobDescription = from.description,
                    keySkillsJSON = keySkillsJSON,
                    contactName = from.contacts?.name,
                    contactEmail = from.contacts?.email,
                    phonesJSON = phonesJSON,
                    lastUpdate = System.currentTimeMillis()
                ),
                if (from.employer == null) {
                    null
                } else {
                    EmployerEntity(
                        id = from.employer!!.id,
                        employerName = from.employer?.name ?: "",
                        logosJSON = logoJSON,
                        employerLogoUri = null
                    )
                }
            )
        }
    }

    private fun map(from: VacancyItemDto, isFavorite: Boolean): DetailedVacancyItem {
        return with(from) {
            DetailedVacancyItem(
                id = id,
                title = name,
                employerName = employer?.name,
                employerLogo = employer?.logoUrls?.medium,
                area = area?.name,
                haveSalary = salary != null,
                salaryFrom = salary?.from,
                salaryTo = salary?.to,
                salaryCurrency = salary?.currency?.symbol,
                experience = experience?.name,
                employment = employment?.name,
                schedule = schedule?.name,
                description = description,
                keySkills = keySkills?.map { it.name ?: "" },
                contacts = map(from.contacts ?: ContactsDto()),
                favorite = isFavorite
            )
        }
    }

    private fun phoneDtoToPhone(from: PhoneDto): Pair<String, String> {
        return with(from) {
            Pair(
                from.comment ?: "",
                (from.country ?: "").toString()
                    + " " + (from.city ?: "").toString()
                    + " " + (from.number ?: "").toString()
            )
        }
    }

    private fun map(from: ContactsDto): Contacts {
        val phones: List<Pair<String, String>>? = from.phones?.map { phoneDtoToPhone(it) }
        return with(from) {
            Contacts(
                name = from.name,
                email = from.email,
                phones = phones
            )
        }
    }

    companion object {
        const val LOGO_SIZE = "240"
    }
}
