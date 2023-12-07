package ru.practicum.android.diploma.vacancy.domain.impl

import ru.practicum.android.diploma.vacancy.domain.models.VacancyItem

interface VacancyInteractor {
    fun getVacancy(id: Int): Pair<VacancyItem, Boolean>
}
