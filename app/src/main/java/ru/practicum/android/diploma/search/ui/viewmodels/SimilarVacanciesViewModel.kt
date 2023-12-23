package ru.practicum.android.diploma.search.ui.viewmodels

import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import ru.practicum.android.diploma.common.data.dto.Resource
import ru.practicum.android.diploma.common.domain.models.NetworkErrors
import ru.practicum.android.diploma.filter.domain.FilterSettingsInteractor
import ru.practicum.android.diploma.search.domain.api.SearchInteractor
import ru.practicum.android.diploma.search.domain.models.VacanciesSearchResult
import ru.practicum.android.diploma.search.ui.viewmodels.states.ErrorsSearchScreenStates
import ru.practicum.android.diploma.search.ui.viewmodels.states.SearchScreenState
import javax.inject.Inject

@HiltViewModel
class SimilarVacanciesViewModel @Inject constructor(
    private val interactor: SearchInteractor,
    private val sharedPrefsInteractor: FilterSettingsInteractor
) : SearchViewModel(
    interactor,
    sharedPrefsInteractor
) {
    private var _state = MutableStateFlow<SearchScreenState>(SearchScreenState.Default())
    override val state: StateFlow<SearchScreenState>
        get() = _state
    private var totalPages: Int = 0
    private var currentPage: Int = 0
    private var isLastUpdatePage = false

    var vacancyId: Int = 0

    fun getVacancyList(id: Int) {
        vacancyId = id
        loadVacancies()
    }

    private fun loadVacancies() {
        _state.value = SearchScreenState.Loading(currentPage)
        viewModelScope.launch {
            interactor.searchSimilarVacancies(vacancyId, currentPage).collect { result ->
                processSearchResult(result)
            }
        }
    }

    private fun processSearchResult(result: Resource<VacanciesSearchResult>) {
        when (result) {
            is Resource.Success -> {
                result.data?.let { data ->
                    totalPages = data.totalPages
                    currentPage = data.currentPage
                    vacancies.addAll(data.vacancies)
                    _state.value = SearchScreenState.Content(
                        totalPages,
                        currentPage,
                        vacancies.size,
                        vacancies.toList()
                    )
                }
            }

            is Resource.Error -> {
                val showSnackbar = vacancies.isNotEmpty()
                when (result.error) {
                    NetworkErrors.NoInternet -> _state.value =
                        SearchScreenState.Error(ErrorsSearchScreenStates.NO_INTERNET, showSnackbar)

                    else -> _state.value = SearchScreenState.Error(ErrorsSearchScreenStates.NO_INTERNET, showSnackbar)
                }
            }
        }
    }

    fun loadMoreVacancies() {
        if (currentPage + 1 < totalPages) {
            currentPage++
            loadVacancies()
        }
    }


    private fun provideResponse(result: Resource<VacanciesSearchResult>) {
        when (result) {
            is Resource.Success -> {
                if (result.data?.vacancies.isNullOrEmpty()) {
                    _state.value = SearchScreenState.Error(ErrorsSearchScreenStates.NOT_FOUND)
                } else if (result.data!!.vacanciesFound > 0) {
                    _state.value = SearchScreenState.Content(
                        result.data.totalPages,
                        result.data.currentPage,
                        result.data.vacancies.toList().size,
                        result.data.vacancies.toList()
                    )
                }
            }

            is Resource.Error -> {
                _state.value = SearchScreenState.Error(
                    when (result.error) {
                        NetworkErrors.NoInternet -> ErrorsSearchScreenStates.NO_INTERNET
                        else -> ErrorsSearchScreenStates.SERVER_ERROR
                    }
                )
            }
        }
    }
}
