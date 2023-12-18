package ru.practicum.android.diploma.filter.ui.viewmodel.states

sealed class FilterViewModelInteraction {
    object clearSettings : FilterViewModelInteraction()
    object saveSettings : FilterViewModelInteraction()
    data class setSalary(val salary: Int?) : FilterViewModelInteraction()
    data class setSalaryOnly(val onlySalary: Boolean) : FilterViewModelInteraction()
}
