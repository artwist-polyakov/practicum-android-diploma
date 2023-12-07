package ru.practicum.android.diploma.search.ui.viewmodels.states

data class SerchSettingsState(
    val currentPage: Int = 0,
    val currentQuery: String = "",
    val currentRegion: Int? = null,
    val currentSalary: Int = 0,
    val currentIndustry: String = "",
    var currentSalaryOnly: Boolean = false,
)
