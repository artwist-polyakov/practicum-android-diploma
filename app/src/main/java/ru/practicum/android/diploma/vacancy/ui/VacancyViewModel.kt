package ru.practicum.android.diploma.vacancy.ui

import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import ru.practicum.android.diploma.common.data.dto.Resource
import ru.practicum.android.diploma.common.ui.BaseViewModel
import ru.practicum.android.diploma.vacancy.domain.api.SingleVacancyInteractor
import ru.practicum.android.diploma.vacancy.domain.models.DetailedVacancyItem
import ru.practicum.android.diploma.vacancy.domain.models.VacancyItem
import ru.practicum.android.diploma.vacancy.domain.models.VacancyState
import javax.inject.Inject

typealias content = Pair<VacancyItem, Boolean>

@HiltViewModel
class VacancyViewModel @Inject constructor(private val interactor: SingleVacancyInteractor) : BaseViewModel() {

    private val _state = MutableStateFlow<VacancyState>(VacancyState.Loading)
    val state: StateFlow<VacancyState>
        get() = _state

    fun getVacancy(id: Int) {
        viewModelScope.launch {
            interactor.getVacancy(id)
                .collect { result ->
                    processResult(result)
                }
        }
    }

    fun interactWithLike(vacancyID: Int) {
        viewModelScope.launch {
            val result = interactor.interactWithVacancyFavor(vacancyID)
            var currentVacancy = (state.value as? VacancyState.Content)?.vacancy
            currentVacancy = currentVacancy?.copy(favorite = result)
            currentVacancy?.let {
                _state.value = VacancyState.Content(it)
            }
        }
    }

    private suspend fun processResult(result: Resource<DetailedVacancyItem>) {
        when (result) {
            is Resource.Success -> {
                result.data?.let {
                    _state.value = VacancyState.Content(it)
                }
            }

            else -> {
                _state.value = VacancyState.ConnectionError
            }
        }
    }

    fun shareVacancy(url: String) {
        interactor.shareVacancy(url)
    }
}
