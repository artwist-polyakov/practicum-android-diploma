package ru.practicum.android.diploma.vacancy.domain.impl

import ru.practicum.android.diploma.vacancy.domain.models.Employer
import ru.practicum.android.diploma.vacancy.domain.models.IdName
import ru.practicum.android.diploma.vacancy.domain.models.Logo
import ru.practicum.android.diploma.vacancy.domain.models.Snippet
import ru.practicum.android.diploma.vacancy.domain.models.VacancyItem

class VacancyInteractorImpl : VacancyInteractor {
    private val vacancyItem = VacancyItem(
        id = MOCK_ID,
        name = "Разработчик Android",
        description = "Разработка и поддержка мобильных приложений на Android",
        department = IdName("1", "Разработка"),
        experience = IdName("2", "От 3 лет"),
        employment = IdName("3", "Полная занятость"),
        area = IdName("4", "Москва"),
        schedule = IdName("5", "Полный день"),
        snippet = Snippet(
            requirements = "Знание Kotlin, опыт работы с Android SDK",
            responsibility = "Разработка новых функций, поддержка существующего кода"
        ),
        salary = null,
        employer = Employer(
            id = 123,
            name = "ООО Рога и Копыта",
            logoUrls = Logo(
                original = "https://example.com/original.jpg",
                little = "https://example.com/little.jpg",
                medium = "https://example.com/medium.jpg"
            )
        ),
        contacts = null,
        keySkills = null
    )

    override fun getVacancy(id: Int): Pair<VacancyItem, Boolean> {
        val isVacancyAvailable = true
        return Pair(vacancyItem, isVacancyAvailable)
    }

    companion object {
        private const val MOCK_ID = 1221 // Моковый id
    }
}
