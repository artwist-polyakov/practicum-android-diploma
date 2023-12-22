package ru.practicum.android.diploma.filter.domain

import kotlinx.coroutines.flow.Flow
import ru.practicum.android.diploma.filter.domain.models.FilterIndustryValue
import ru.practicum.android.diploma.filter.domain.models.FilterRegionValue
import ru.practicum.android.diploma.filter.ui.viewmodel.states.FilterSettingsUIState
import ru.practicum.android.diploma.search.ui.viewmodels.states.SearchSettingsState

@Suppress("TooManyFunctions")
interface FilterSettingsInteractor {
    suspend fun setRegion(id: Int?, name: String?)

    suspend fun getRegion(): FilterRegionValue

    suspend fun setIndustry(id: String?, name: String?)

    suspend fun getIndustry(): FilterIndustryValue

    suspend fun setSalary(id: Int?)

    suspend fun getSalary(): Int?

    suspend fun setWithSalaryOnly(state: Boolean)

    suspend fun getWithSalaryOnly(): Boolean

    suspend fun saveSettings()

    suspend fun resetSettings()

    suspend fun restoreSettings()

    fun getSearchSettings(): Flow<SearchSettingsState>

    fun getFilterUISettings(): Flow<FilterSettingsUIState>
}
