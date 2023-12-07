package ru.practicum.android.diploma.search.ui.viewmodels.states

import ru.practicum.android.diploma.common.domain.models.NetworkErrors

sealed class SearchScreenState {

    // todo заменить в двух местах Any, когда будет понятно
    //  нужно ли хранить предыщуие сущности на экране и что отображаем
    data object Loading : SearchScreenState()
    data class Error(val error: ErrorsSearchScreenStates) : SearchScreenState()
    data class Content(val totalPages: Int, val currentPage: Int, val vacancies: Any) : SearchScreenState()
}
