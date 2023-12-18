package ru.practicum.android.diploma.filter.ui.viewmodel.states

sealed class FilterScreenState {
    object Empty : FilterScreenState()
    data class Settled(
        val region: String = "",
        val industry: String = "",
        val salary: Int? = null,
        val withSalaryOnly: Boolean = false,
        val isResetButtonEmabled: Boolean = false,
        val isApplyButtonEnabled: Boolean = false
    ) : FilterScreenState()
}
