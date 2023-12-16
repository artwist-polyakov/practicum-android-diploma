package ru.practicum.android.diploma.filter.ui.viewmodel

import android.util.Log
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import ru.practicum.android.diploma.common.data.dto.Resource
import ru.practicum.android.diploma.common.domain.models.NetworkErrors
import ru.practicum.android.diploma.common.ui.BaseViewModel
import ru.practicum.android.diploma.filter.domain.FilterSettingsInteractor
import ru.practicum.android.diploma.filter.domain.models.FilterRegionValue
import ru.practicum.android.diploma.filter.ui.viewmodel.states.SearchRegionScreenState
import ru.practicum.android.diploma.search.domain.api.SearchInteractor
import ru.practicum.android.diploma.search.domain.models.SingleTreeElement
import ru.practicum.android.diploma.search.ui.viewmodels.states.ErrorsSearchScreenStates
import javax.inject.Inject

typealias regionList = List<SingleTreeElement>

@HiltViewModel
open class WorkPlaceViewModel @Inject constructor(
    private val searchInteractor: SearchInteractor,
    private val filterInteractor: FilterSettingsInteractor
) : BaseViewModel() {
    private var _state = MutableStateFlow<SearchRegionScreenState>(SearchRegionScreenState.Loading)
    val state: StateFlow<SearchRegionScreenState>
        get() = _state

    fun getAreas() {
        viewModelScope.launch {
            searchInteractor.getAreas().collect { result ->
                provideResponse(result)
            }
        }
    }

    fun getAreas(forId: Int) {
        viewModelScope.launch {
            searchInteractor.getAreas(forId).collect { result ->
                provideResponse(result)
            }
        }
    }

    private fun provideResponse(result: Resource<regionList>) {
        when (result) {
            is Resource.Success -> {
                if (result.data.isNullOrEmpty()) {
                    _state.value = SearchRegionScreenState.Error(ErrorsSearchScreenStates.FAIL_FETCH_REGIONS)
                } else {
                    _state.value = SearchRegionScreenState.Content(result.data)
                    Log.i("WorkPlaceVMMyLog", "provideResponse status data = ${result.data}")
                }
            }

            is Resource.Error -> {
                _state.value = SearchRegionScreenState.Error(
                    when (result.error) {
                        NetworkErrors.NoInternet -> ErrorsSearchScreenStates.NO_INTERNET
                        else -> ErrorsSearchScreenStates.SERVER_ERROR
                    }
                )
            }
        }
    }

    fun updateStateWithCountry(id: String, name: String) {
        viewModelScope.launch {
            val currentState = _state.value
            if (currentState is SearchRegionScreenState.Content) {
                _state.value = currentState.copy(
                    selectedCountry = FilterRegionValue(id.toInt(), name)
                )
                Log.i("WorkPlaceVMMyLog", "updateStateWithCountry currentState = ${currentState.selectedCountry}")
            }
            Log.i("WorkPlaceVMMyLog", "updateStateWithCountry state = ${currentState}")

            filterInteractor.setRegion(id.toInt(), name)
        }
    }

    fun updateStateWithRegion(id: String, name: String) {
        viewModelScope.launch {
            val currentState = _state.value
            if (currentState is SearchRegionScreenState.Content) {
                _state.value = currentState.copy(
                    selectedRegion = FilterRegionValue(id.toInt(), name)
                )
            }
            filterInteractor.setRegion(id.toInt(), name)
        }
    }
}
