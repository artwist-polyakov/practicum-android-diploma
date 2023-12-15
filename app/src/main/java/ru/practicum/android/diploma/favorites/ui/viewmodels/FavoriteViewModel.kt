package ru.practicum.android.diploma.favorites.ui.viewmodels

import android.util.Log
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch
import ru.practicum.android.diploma.common.ui.BaseViewModel
import ru.practicum.android.diploma.favorites.domain.api.FavoritesDBInteractor
import ru.practicum.android.diploma.favorites.ui.viewmodels.states.FavoritesScreenState
import ru.practicum.android.diploma.search.domain.models.VacancyGeneral
import javax.inject.Inject

@HiltViewModel
class FavoriteViewModel @Inject constructor(
    private val interactor: FavoritesDBInteractor
) : BaseViewModel() {
    private var vacancies: MutableList<VacancyGeneral> = mutableListOf()

    private var totalPages: Int = 0
    private var currentPage: Int = 0

    private var _state = MutableStateFlow<FavoritesScreenState>(FavoritesScreenState.Empty())
        get() = _state

    init {
        Log.d("FavoritesViewModel", "init")
        checkState()
    }

    private fun checkState() {
        viewModelScope.launch {
            interactor.getFavoritesVacancies(page = currentPage)
                .catch {
                    _state.value = FavoritesScreenState.Error()
                }
                .collect {
                    if (it.vacancies.isEmpty()) {
                        totalPages = 0
                        currentPage = 0
                        vacancies.clear()
                        _state.value = FavoritesScreenState.Empty()
                        Log.d("FavoritesViewModel", "Favorites is empty")
                    } else {
                        totalPages = it.totalPages
                        currentPage = it.currentPage
                        vacancies.addAll(it.vacancies)
                        _state.value = FavoritesScreenState.Content(
                            totalPages = totalPages,
                            currentPage = currentPage,
                            totalVacancies = it.vacanciesFound,
                            vacancies = vacancies
                        )
                        Log.d("FavoritesViewModel", "Favorites is not empty")
                        Log.d("FavoritesViewModel", "Total: ${it.vacanciesFound}")
                        Log.d("FavoritesViewModel", "Vacancies: $vacancies")
                    }
                }

        }
    }


}
