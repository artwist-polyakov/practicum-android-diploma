package ru.practicum.android.diploma.search.ui.viewmodels.states

// todo сделать зарплату по дефолту false когда будет фильтр
data class SearchSettingsState(
    val currentPage: Int = 0,
    val currentQuery: String = "",
    val currentRegion: Int? = null,
    val currentSalary: Int? = null,
    val currentIndustry: String? = null,
    val currentSalaryOnly: Boolean = true,
)
