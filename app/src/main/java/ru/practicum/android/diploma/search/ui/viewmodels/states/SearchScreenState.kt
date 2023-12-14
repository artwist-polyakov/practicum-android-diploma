package ru.practicum.android.diploma.search.ui.viewmodels.states

import ru.practicum.android.diploma.search.domain.models.VacancyGeneral

sealed class SearchScreenState: BaseScreenState() {

    // todo заменить в двух местах Any, когда будет понятно
    //  нужно ли хранить предыщуие сущности на экране и что отображаем
    data class Default(override val isFilterEnabled: Boolean = false) : SearchScreenState()
    data class Loading(val forPage: Int = 0, override val isFilterEnabled: Boolean = false) : SearchScreenState()
    data class Error(
        val error: ErrorsSearchScreenStates,
        val showSnackBar: Boolean = false,
        override val isFilterEnabled: Boolean = false
    ) : SearchScreenState()

    data class Content(
        val totalPages: Int,
        val currentPage: Int,
        val totalVacancies: Int,
        val vacancies: List<VacancyGeneral>,
        override val isFilterEnabled: Boolean = false
    ) : SearchScreenState()
}
