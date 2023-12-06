package ru.practicum.android.diploma.vacancy.ui

import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import ru.practicum.android.diploma.common.ui.BaseViewModel
import ru.practicum.android.diploma.vacancy.domain.impl.VacancyInteractor
import ru.practicum.android.diploma.vacancy.domain.models.VacancyItem
import ru.practicum.android.diploma.vacancy.domain.models.VacancyState
import javax.inject.Inject

typealias content = Pair<VacancyItem, Boolean>

@HiltViewModel
class VacancyViewModel @Inject constructor(private val interactor: VacancyInteractor) : BaseViewModel() {

    private val _state = MutableStateFlow<VacancyState>(VacancyState.Loading)
    val state: StateFlow<VacancyState>
        get() = _state

    fun getVacancy(id: Int) {
        viewModelScope.launch {
            try {
                val content = interactor.getVacancy(id)
                _state.emit(VacancyState.Content(content.first, content.second))
            } catch (e: Exception) {
                _state.emit(VacancyState.ConnectionError)
            }
        }
    }
}
