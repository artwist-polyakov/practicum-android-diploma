package ru.practicum.android.diploma.search.ui.viewmodels.states

sealed class ViewModelInteractionState {
    data class setRegion(val region: Int) : ViewModelInteractionState()
    data class setIndustry(val industry: String) : ViewModelInteractionState()
    data class setSalary(val salary: Int) : ViewModelInteractionState()
    data class setSalaryOnly(val salaryOnly: Boolean) : ViewModelInteractionState()
    data class setQuery(val query: String) : ViewModelInteractionState()
    data class setPage(val page: Int) : ViewModelInteractionState()
}
