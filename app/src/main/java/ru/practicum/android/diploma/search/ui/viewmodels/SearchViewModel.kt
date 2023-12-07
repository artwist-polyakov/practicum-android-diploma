package ru.practicum.android.diploma.search.ui.viewmodels

import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import ru.practicum.android.diploma.common.domain.models.NetworkErrors
import ru.practicum.android.diploma.common.ui.BaseViewModel
import ru.practicum.android.diploma.common.utils.debounce
import ru.practicum.android.diploma.search.domain.api.SearchInteractor
import ru.practicum.android.diploma.search.domain.models.VacancyGeneral
import ru.practicum.android.diploma.search.ui.viewmodels.states.ErrorsSearchScreenStates
import ru.practicum.android.diploma.search.ui.viewmodels.states.SearchScreenState
import ru.practicum.android.diploma.search.ui.viewmodels.states.SerchSetingsState
import javax.inject.Inject

@HiltViewModel
class SearchViewModel @Inject constructor(
    private val interactor: SearchInteractor
) : BaseViewModel() {

    private var searchSettings: SerchSetingsState = SerchSetingsState()

    // todo кажется можно не хранить их тут — так как вакансии будут в стейте
    private var vacancies: MutableList<VacancyGeneral> = mutableListOf()

    private var _state = MutableStateFlow<SearchScreenState>(SearchScreenState.Error(ErrorsSearchScreenStates.EMPTY))
    val state: StateFlow<SearchScreenState>
        get() = _state

    private val searchDebounce = debounce<String>(
        SEARCH_DEBOUNCE_DELAY,
        viewModelScope,
        true
    ) { text ->
        getVacancies(text)
    }

    fun getVacancies(query: String, page: Int, ) {
        if (currentQuery == query) {
            return
        }

        currentQuery = query
        if (currentQuery.isEmpty()) {
            _state.value = SearchScreenState.Error(ErrorsSearchScreenStates.EMPTY)
            return
        }

        _state.value = SearchScreenState.Loading

        viewModelScope.launch {
            interactor.searchVacancies(text = currentQuery)
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
                        currentPage = result.data.currentPage
                        vacancies = result.data.vacancies.toMutableList()
                        renderState(
                            SearchScreenState.Content(
                                result.data.totalPages,
                                currentPage,
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
                text = currentQuery,
                page = currentPage
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
                    } else if (result.data!!.vacanciesFound > 0 && result.data.currentPage > currentPage) {
                        currentPage = result.data.currentPage
                        vacancies.addAll(result.data.vacancies)
                        renderState(
                            SearchScreenState.Content(
                                result.data.totalPages,
                                currentPage,
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

    companion object {
        private const val SEARCH_DEBOUNCE_DELAY = 2000L
    }
}
