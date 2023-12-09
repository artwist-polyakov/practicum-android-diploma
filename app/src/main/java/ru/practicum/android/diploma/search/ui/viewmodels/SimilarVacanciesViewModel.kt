package ru.practicum.android.diploma.search.ui.viewmodels

import android.util.Log
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch
import ru.practicum.android.diploma.common.data.dto.Resource
import ru.practicum.android.diploma.common.domain.models.NetworkErrors
import ru.practicum.android.diploma.common.ui.BaseViewModel
import ru.practicum.android.diploma.search.domain.api.SearchInteractor
import ru.practicum.android.diploma.search.domain.models.VacanciesSearchResult
import ru.practicum.android.diploma.search.ui.viewmodels.states.ErrorsSearchScreenStates
import ru.practicum.android.diploma.search.ui.viewmodels.states.SearchScreenState
import javax.inject.Inject

@HiltViewModel
class SimilarVacanciesViewModel @Inject constructor(private val interactor: SearchInteractor) : BaseViewModel() {
    private var _state = MutableStateFlow<SearchScreenState>(SearchScreenState.Loading)
    val state: StateFlow<SearchScreenState>
        get() = _state

    var vacancyId: Int = 0

    fun getVacancyList(id: Int) {
        viewModelScope.launch {
            try {
                interactor.searchSimilarVacancies(id)
                    .collect {result ->
                        provideResponse(result)
                    }

            } catch (e: Exception) {
                Log.e("Coroutine Exception", e.stackTraceToString())
            }
        }
    }

    private fun provideResponse(result : Resource<VacanciesSearchResult>) {
        when (result) {
            is Resource.Success -> {
                if (result.data?.vacancies.isNullOrEmpty()) {
                    _state.value = SearchScreenState.Error(ErrorsSearchScreenStates.NOT_FOUND)
                } else if (result.data!!.vacanciesFound > 0) {
                    _state.value = SearchScreenState.Content(
                        result.data.totalPages,
                        result.data.currentPage,
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
