package ru.practicum.android.diploma.vacancy.domain.impl

import ru.practicum.android.diploma.vacancy.domain.models.Contacts
import ru.practicum.android.diploma.vacancy.domain.models.Employer
import ru.practicum.android.diploma.vacancy.domain.models.IdName
import ru.practicum.android.diploma.vacancy.domain.models.Logo
import ru.practicum.android.diploma.vacancy.domain.models.Salary
import ru.practicum.android.diploma.vacancy.domain.models.Snippet
import ru.practicum.android.diploma.vacancy.domain.models.VacancyItem

class VacancyInteractorImpl : VacancyInteractor {
    override fun getVacancy(id: Int): Pair<VacancyItem, Boolean> {
        val vacancyItem = VacancyItem(
            id = id,
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
            salary = Salary(
                from = 100000,
                to = 150000,
                currency = "RUB",
                gross = true
            ),
            employer = Employer(
                id = 123,
                name = "ООО Рога и Копыта",
                logoUrls = Logo(
                    original = "https://example.com/original.jpg",
                    little = "https://example.com/little.jpg",
                    medium = "https://example.com/medium.jpg"
                )
            ),
            contacts = Contacts(

            ),
            keySkills = listOf(
                IdName("6", "Kotlin"),
                IdName("7", "Android SDK"),
                IdName("8", "MVVM")
            )
        )

        val isVacancyAvailable = true // или false в зависимости от логики

        return Pair(vacancyItem, isVacancyAvailable)
    }
}
