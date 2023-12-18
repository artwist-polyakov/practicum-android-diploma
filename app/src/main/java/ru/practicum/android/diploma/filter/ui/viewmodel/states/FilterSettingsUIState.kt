package ru.practicum.android.diploma.filter.ui.viewmodel.states

data class FilterSettingsUIState(
    val region: String? = null,
    val industry: String? = null,
    val salary: Int? = null,
    val salaryOnly: Boolean = false,
)
