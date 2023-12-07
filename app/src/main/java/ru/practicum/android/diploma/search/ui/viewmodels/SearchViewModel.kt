package ru.practicum.android.diploma.search.ui.viewmodels

import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import ru.practicum.android.diploma.common.domain.models.NetworkErrors
import ru.practicum.android.diploma.common.ui.BaseViewModel
import ru.practicum.android.diploma.search.domain.api.SearchInteractor
import ru.practicum.android.diploma.search.domain.models.VacancyGeneral
import ru.practicum.android.diploma.search.ui.viewmodels.states.ErrorsSearchScreenStates
import ru.practicum.android.diploma.search.ui.viewmodels.states.SearchScreenState
import javax.inject.Inject

@HiltViewModel
class SearchViewModel @Inject constructor(
    private val interactor: SearchInteractor
) : BaseViewModel() {
    private var latestPage = 1
    private var latestQuery = ""
    private var vacancies: MutableList<VacancyGeneral> = mutableListOf()

    private var _state = MutableStateFlow<SearchScreenState>(SearchScreenState.Default)
    val state: StateFlow<SearchScreenState>
        get() = _state

    fun getVacancies(query: String) {
        if (latestQuery == query) {
            return
        }

        latestQuery = query
        if (latestQuery.isEmpty()) {
            _state.value = SearchScreenState.Default
            return
        }

        _state.value = SearchScreenState.Loading

        viewModelScope.launch {
            interactor.searchVacancies(text = latestQuery)
                .collect { result ->
                    if (result.error is NetworkErrors) {
                        renderState(
                            SearchScreenState.Error(
                                when (result.error) {
                                    NetworkErrors.ServerError -> ErrorsSearchScreenStates.SERVER_ERROR
                                    NetworkErrors.NoInternet -> ErrorsSearchScreenStates.NO_INTERNET
                                    else -> ErrorsSearchScreenStates.SERVER_ERROR
                                }
                            )
                        )
                    } else if (result.data?.vacancies.isNullOrEmpty()) {
                        renderState(SearchScreenState.Error(ErrorsSearchScreenStates.NOT_FOUND))
                    } else if (result.data!!.vacanciesFound > 0) {
                        latestPage = result.data.currentPage
                        vacancies = result.data.vacancies.toMutableList()
                        renderState(
                            SearchScreenState.Content(
                                result.data.totalPages,
                                latestPage,
                                vacancies.toList()
                            )
                        )
                    }
                }
        }
    }

    fun addVacancies() {
        viewModelScope.launch {
            interactor.searchVacancies(
                text = latestQuery,
                page = latestPage
            )
                .collect { result ->
                    if (result.error is NetworkErrors) {
                        renderState(
                            SearchScreenState.Error(
                                when (result.error) {
                                    NetworkErrors.ServerError -> ErrorsSearchScreenStates.SERVER_ERROR
                                    NetworkErrors.NoInternet -> ErrorsSearchScreenStates.NO_INTERNET
                                    else -> ErrorsSearchScreenStates.SERVER_ERROR
                                }
                            )
                        )
                    } else if (result.data?.vacancies.isNullOrEmpty()) {
                        renderState(SearchScreenState.Error(ErrorsSearchScreenStates.NOT_FOUND))
                    } else if (result.data!!.vacanciesFound > 0 && result.data.currentPage > latestPage) {
                        latestPage = result.data.currentPage
                        vacancies.addAll(result.data.vacancies)
                        renderState(
                            SearchScreenState.Content(
                                result.data.totalPages,
                                latestPage,
                                vacancies.toList()
                            )
                        )
                    }
                }
        }
    }

    private suspend fun renderState(state: SearchScreenState) {
        _state.emit(state)
    }
}
