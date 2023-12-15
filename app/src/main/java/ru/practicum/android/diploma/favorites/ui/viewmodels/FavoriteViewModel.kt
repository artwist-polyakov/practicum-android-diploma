package ru.practicum.android.diploma.favorites.ui.viewmodels

import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
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
    private var showSnackBar: Boolean = false

    private var totalPages: Int = 0
    private var isLastUpdatePage = false

    private var _state = MutableStateFlow<FavoritesScreenState>(FavoritesScreenState.Empty())
        get() = _state

}
