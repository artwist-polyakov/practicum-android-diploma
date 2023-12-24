package ru.practicum.android.diploma.favorites.ui.viewmodels

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
import ru.practicum.android.diploma.vacancy.domain.api.SingleVacancyInteractor
import ru.practicum.android.diploma.vacancy.ui.VacancyViewModel
import javax.inject.Inject

@HiltViewModel
class FavoriteViewModel @Inject constructor(
    private val interactor: FavoritesDBInteractor,
    private val vacancyInteractor: SingleVacancyInteractor
) : BaseViewModel() {
    private val vacancies: MutableList<VacancyGeneral> = mutableListOf()

    private var totalPages: Int = 0
    private var currentPage: Int = 0

    private var _state = MutableStateFlow<FavoritesScreenState>(FavoritesScreenState.Empty())

    val state: MutableStateFlow<FavoritesScreenState>
        get() = _state

    fun handleRequest() {
        viewModelScope.launch {
            if (currentPage < totalPages - 1 || currentPage == 0) {
                setLoadingState()
                loadVacancies()
            }
        }
    }

    fun deleteFromFavorites(vacancy: VacancyGeneral): Boolean {
        viewModelScope.launch {
            interactor.deleteVacancy(vacancy.id)
            updateStateWithContent(VacanciesSearchResult(vacancies.size, currentPage, totalPages, vacancies))
        }
        return true
    }

    fun shareVacancy(vacancy: VacancyGeneral) {
        vacancyInteractor.shareVacancy(VacancyViewModel.BASE_URL + vacancy.id.toString())
    }

    private fun setLoadingState() {
        _state.value = FavoritesScreenState.Loading(isBottomIndicator = currentPage != 0)
    }

    private suspend fun loadVacancies() {
        interactor.getFavoritesVacancies(
            page = currentPage + if (currentPage < totalPages - 1) 1 else 0
        ).catch {
            handleError()
        }.collect {
            processResult(it)
        }
    }

    private fun processResult(result: VacanciesSearchResult) {
        if (result.vacancies.isEmpty()) {
            resetState()
        } else {
            updateStateWithContent(result)
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

    private fun updateStateWithContent(result: VacanciesSearchResult) {
        if (result.currentPage == 0) {
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
    }
}
