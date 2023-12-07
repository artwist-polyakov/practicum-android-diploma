package ru.practicum.android.diploma.vacancy.ui

import android.util.Log
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import retrofit2.HttpException
import ru.practicum.android.diploma.common.data.dto.Resource
import ru.practicum.android.diploma.common.ui.BaseViewModel
import ru.practicum.android.diploma.search.domain.api.SingleVacancyInteractor
import ru.practicum.android.diploma.search.domain.models.DetailedVacancyItem
import ru.practicum.android.diploma.vacancy.domain.impl.VacancyInteractor
import ru.practicum.android.diploma.vacancy.domain.models.VacancyItem
import ru.practicum.android.diploma.vacancy.domain.models.VacancyState
import java.io.IOException
import javax.inject.Inject

typealias content = Pair<VacancyItem, Boolean>

@HiltViewModel
class VacancyViewModel @Inject constructor(private val interactor: SingleVacancyInteractor) : BaseViewModel() {

    private val _state = MutableStateFlow<VacancyState>(VacancyState.Loading)
    val state: StateFlow<VacancyState>
        get() = _state

    @Suppress("TooGenericExceptionCaught")
    fun getVacancy(id: Int) {
        viewModelScope.launch {
            interactor.getVacancy(id)
                .collect { result ->
                    processResult(result)
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
        _state.emit(_state.value)
    }
}
