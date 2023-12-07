package ru.practicum.android.diploma.search.ui.viewmodels

import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import ru.practicum.android.diploma.common.domain.models.NetworkErrors
import ru.practicum.android.diploma.common.ui.BaseViewModel
import ru.practicum.android.diploma.search.domain.api.SearchInteractor
import ru.practicum.android.diploma.search.ui.viewmodels.states.SearchScreenState
import javax.inject.Inject

@HiltViewModel
class SearchViewModel @Inject constructor(private val interactor: SearchInteractor) : BaseViewModel() {
    private var _state = MutableStateFlow<SearchScreenState>(SearchScreenState.Loading)
    val state: StateFlow<SearchScreenState>
        get() = _state

    fun getVacancies(query: String) {
        viewModelScope.launch {
            interactor.searchVacancies(text = query)
                .collect { result ->
                    if (result.error is NetworkErrors) {
                        renderState(SearchScreenState.Error(result.error))
                    } else if (result.data?.vacancies.isNullOrEmpty()) {
                        renderState(SearchScreenState.Empty("message"))
                    } else if (result.data!!.vacanciesFound > 0) {
                        renderState(
                            SearchScreenState.Content(
                                result.data.totalPages,
                                result.data.currentPage,
                                (result.data.vacancies
                                    )
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
