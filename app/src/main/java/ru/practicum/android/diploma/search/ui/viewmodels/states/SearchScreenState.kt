package ru.practicum.android.diploma.search.ui.viewmodels.states

sealed class SearchScreenState {

    // todo заменить в двух местах Any, когда будет понятно
    //  нужно ли хранить предыщуие сущности на экране и что отображаем
    data class Loading(val previousPage: Any) : SearchScreenState()
    data class Error(val error: ErrorsSearchScreenStates) : SearchScreenState()
    data class Result(val totalPages: Int, val currentPage: Int, val vacancies: Any) : SearchScreenState()
}
