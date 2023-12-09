package ru.practicum.android.diploma.search.ui.viewmodels

import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import ru.practicum.android.diploma.common.ui.BaseViewModel
import ru.practicum.android.diploma.search.domain.api.SearchInteractor
import ru.practicum.android.diploma.search.ui.viewmodels.states.SearchScreenState
import javax.inject.Inject

@HiltViewModel
class SimilarVacanciesViewModel @Inject constructor(private val interactor: SearchInteractor) : BaseViewModel() {
    private var _state = MutableStateFlow<SearchScreenState>(SearchScreenState.Loading)
    val state: StateFlow<SearchScreenState>
        get() = _state
}
