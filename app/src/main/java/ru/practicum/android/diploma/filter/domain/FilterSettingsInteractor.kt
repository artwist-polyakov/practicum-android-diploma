package ru.practicum.android.diploma.filter.domain

import ru.practicum.android.diploma.filter.domain.models.FilterFieldValue

interface FilterSettingsInteractor {
    fun setRegion(id: Int?, name: String?)

    fun getRegion(): FilterFieldValue

    fun setIndustry(id: Int?, name: String?)

    fun getIndustry(): FilterFieldValue

    fun setSalary(id: Int?)

    fun getSalary(): Int?

    fun setWithSalaryOnly(state: Boolean)

    fun getWithSalaryOnly(): Boolean

    fun resetSettings()
}
