package ru.practicum.android.diploma.filter.domain

import kotlinx.coroutines.flow.Flow
import ru.practicum.android.diploma.filter.domain.models.FilterIndustryValue
import ru.practicum.android.diploma.filter.domain.models.FilterRegionValue
import ru.practicum.android.diploma.search.ui.viewmodels.states.SearchSettingsState

interface FilterSettingsInteractor {
    fun setRegion(id: Int?, name: String?)

    fun getRegion(): FilterRegionValue

    fun setIndustry(id: String?, name: String?)

    fun getIndustry(): FilterIndustryValue

    fun setSalary(id: Int?)

    fun getSalary(): Int?

    fun setWithSalaryOnly(state: Boolean)

    fun getWithSalaryOnly(): Boolean

    fun resetSettings()

    fun getSearchSettings(): Flow<SearchSettingsState>
}
