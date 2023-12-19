package ru.practicum.android.diploma.favorites.ui.viewmodels

import android.util.Log
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.launch
import ru.practicum.android.diploma.common.ui.BaseViewModel
import ru.practicum.android.diploma.favorites.domain.api.FavoritesDBInteractor
import ru.practicum.android.diploma.favorites.ui.viewmodels.states.FavoritesScreenState
import ru.practicum.android.diploma.search.domain.models.VacanciesSearchResult
import ru.practicum.android.diploma.search.domain.models.VacancyGeneral
import ru.practicum.android.diploma.search.ui.viewmodels.states.SearchSettingsState
import javax.inject.Inject

@HiltViewModel
class FavoriteViewModel @Inject constructor(
    private val interactor: FavoritesDBInteractor
) : BaseViewModel() {
    private var vacancies: MutableList<VacancyGeneral> = mutableListOf()

    private var totalPages: Int = 0
    private var currentPage: Int = 0

    private var _state = MutableStateFlow<FavoritesScreenState>(FavoritesScreenState.Empty())

    val state: MutableStateFlow<FavoritesScreenState>
        get() = _state

    private var searchSettings: SearchSettingsState = SearchSettingsState()
    private var isLastUpdatePage = false

    fun handleRequest(nextPage: Boolean = false) {
        viewModelScope.launch {
            if (currentPage < totalPages || currentPage == 0) {
                setLoadingState()
                loadVacancies(nextPage)
            }
        }
    }

    private fun setLoadingState() {
        _state.value = FavoritesScreenState.Loading(isBottomIndicator = currentPage != 0)
    }

    private suspend fun loadVacancies(nextPage: Boolean) {
        interactor.getFavoritesVacancies(
            page = currentPage + if (nextPage && currentPage < totalPages) 1 else 0
        )
            .catch { handleError() }
            .collect { processResult(it, nextPage) }
    }

    private fun processResult(result: VacanciesSearchResult, nextPage: Boolean) {
        if (result.vacancies.isEmpty()) {
            resetState()
        } else {
            updateStateWithContent(result, nextPage)
        }
    }

    private fun handleError() {
        _state.value = FavoritesScreenState.Error()
    }

    private fun resetState() {
        totalPages = 0
        currentPage = 0
        vacancies.clear()
        _state.value = FavoritesScreenState.Empty()
    }

    private fun updateStateWithContent(result: VacanciesSearchResult, nextPage: Boolean) {
        if (!nextPage) {
            vacancies.clear()
        }
        totalPages = result.totalPages
        currentPage = result.currentPage
        vacancies.addAll(result.vacancies)
        _state.value = FavoritesScreenState.Content(
            totalPages = totalPages,
            currentPage = currentPage,
            totalVacancies = result.vacanciesFound,
            vacancies = vacancies
        )

        Log.d("Стейт totalPages", totalPages.toString())
        Log.d("Стейт currentPage", currentPage.toString())
        Log.d("Стейт vacanciesFound", result.vacanciesFound.toString())
    }
}
