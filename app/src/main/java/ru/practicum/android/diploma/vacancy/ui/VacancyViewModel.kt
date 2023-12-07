package ru.practicum.android.diploma.vacancy.ui

import android.util.Log
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import retrofit2.HttpException
import ru.practicum.android.diploma.common.ui.BaseViewModel
import ru.practicum.android.diploma.vacancy.domain.impl.VacancyInteractor
import ru.practicum.android.diploma.vacancy.domain.models.VacancyItem
import ru.practicum.android.diploma.vacancy.domain.models.VacancyState
import java.io.IOException
import javax.inject.Inject

typealias content = Pair<VacancyItem, Boolean>

@HiltViewModel
class VacancyViewModel @Inject constructor(private val interactor: VacancyInteractor) : BaseViewModel() {

    private val _state = MutableStateFlow<VacancyState>(VacancyState.Loading)
    val state: StateFlow<VacancyState>
        get() = _state

    @Suppress("TooGenericExceptionCaught")
    fun getVacancy(id: Int) {
        viewModelScope.launch {
            try {
                val content = interactor.getVacancy(id)
                _state.emit(VacancyState.Content(content.first, content.second))
            } catch (e: IOException) {
                Log.e("IOException", "IOException: ${e.message}")
                _state.emit(VacancyState.ConnectionError)
            } catch (e: HttpException) {
                Log.e("HttpException", "HttpException: ${e.message}")
                _state.emit(VacancyState.ConnectionError)
            } catch (e: RuntimeException) {
                Log.e("RuntimeException", "RuntimeException: ${e.message}")
                _state.emit(VacancyState.ConnectionError)
            }
        }
    }
}
