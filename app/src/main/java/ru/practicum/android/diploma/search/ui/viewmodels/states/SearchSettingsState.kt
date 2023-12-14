package ru.practicum.android.diploma.search.ui.viewmodels.states

data class SearchSettingsState(
    val currentPage: Int = 0,
    val currentQuery: String = "",
    val currentRegion: Int? = null,
    val currentSalary: Int? = null,
    val currentIndustry: String? = null,
    val currentSalaryOnly: Boolean = true,
)
