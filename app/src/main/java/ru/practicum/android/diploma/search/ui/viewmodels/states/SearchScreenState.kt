package ru.practicum.android.diploma.search.ui.viewmodels.states

import ru.practicum.android.diploma.search.domain.models.VacancyGeneral

sealed class SearchScreenState {

    // todo заменить в двух местах Any, когда будет понятно
    //  нужно ли хранить предыщуие сущности на экране и что отображаем
    data object Default : SearchScreenState()
    data object Loading : SearchScreenState()
    data class Error(val error: ErrorsSearchScreenStates) : SearchScreenState()
    data class Content(
        val totalPages: Int,
        val currentPage: Int,
        val vacancies: List<VacancyGeneral>
    ) : SearchScreenState()
}
