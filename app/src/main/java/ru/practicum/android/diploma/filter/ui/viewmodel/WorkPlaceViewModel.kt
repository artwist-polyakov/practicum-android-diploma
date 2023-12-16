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

@HiltViewModel
class WorkPlaceViewModel @Inject constructor(
    private val searchInteractor: SearchInteractor,
    private val filterInteractor: FilterSettingsInteractor
) : BaseViewModel() {
    private var _state = MutableStateFlow<SearchRegionScreenState>(SearchRegionScreenState.Loading)
    val state: StateFlow<SearchRegionScreenState>
        get() = _state

    private var country: FilterRegionValue? = null

    fun getAreas() {
        viewModelScope.launch {
            searchInteractor.getAreas().collect { result ->
                provideResponse(result)
            }
        }
    }

    fun getRegions() {
        viewModelScope.launch {
            searchInteractor.getAreas(country?.id).collect { result ->
                provideResponse(result)
            }
        }
    }

    private fun provideResponse(result: Resource<List<SingleTreeElement>>) {
        when (result) {
            is Resource.Success -> {
                if (result.data.isNullOrEmpty()) {
                    _state.value = SearchRegionScreenState.Error(ErrorsSearchScreenStates.FAIL_FETCH_REGIONS)
                } else {
                    val currentState = _state.value as? SearchRegionScreenState.Content
                    _state.value = currentState?.copy(regions = result.data)
                        ?: SearchRegionScreenState.Content(regions = result.data)
                    Log.i(MY_LOG, "regionList result = ${result.data}")
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

    /**
     * Метод обновляет значение в state для selectedCountry и удаляет selectedRegion если значение selectedCountry изменилось
     **/
    fun updateStateWithCountry(id: String, name: String) {
        viewModelScope.launch {
            val currentState = _state.value
            if (currentState is SearchRegionScreenState.Content) {
                if (country != FilterRegionValue(id.toInt(), name)) {
                    _state.value = currentState.copy(
                        selectedCountry = FilterRegionValue(id.toInt(), name),
                        selectedRegion = null
                    )
                    country = FilterRegionValue(id.toInt(), name)
                }

                val updatedState = _state.value as SearchRegionScreenState.Content
                Log.i(MY_LOG, "updateStateWithRegion selectedCountry = ${updatedState.selectedCountry}")
                Log.i(MY_LOG, "updateStateWithCountry currentState regions = ${currentState.regions}")
            }
            Log.i(MY_LOG, "updateStateWithCountry state = $currentState")

            filterInteractor.setRegion(id.toInt(), name)
        }
    }

    /**
     * Метод обновляет значение в state selectedRegion
     **/
    fun updateStateWithRegion(id: String, name: String) {
        viewModelScope.launch {
            val currentState = _state.value
            if (currentState is SearchRegionScreenState.Content) {
                _state.value = currentState.copy(
                    selectedRegion = FilterRegionValue(id.toInt(), name)
                )
                val updatedState = _state.value as SearchRegionScreenState.Content
                Log.i(MY_LOG, "updateStateWithRegion selectedRegion = ${updatedState.selectedRegion}")
                Log.i(MY_LOG, "updateStateWithRegion currentState regions = ${currentState.regions}")
            }
            filterInteractor.setRegion(id.toInt(), name)
        }
    }

    fun clearselectedCountry() {
        viewModelScope.launch {
            val currentState = _state.value
            if (currentState is SearchRegionScreenState.Content) {
                _state.value = currentState.copy(
                    selectedCountry = null
                )
            }
            filterInteractor.setRegion(null, null)
            country = null
            clearselectedRegion()
        }
    }

    fun clearselectedRegion() {
        viewModelScope.launch {
            val currentState = _state.value
            if (currentState is SearchRegionScreenState.Content) {
                _state.value = currentState.copy(
                    selectedRegion = null
                )
            }
            filterInteractor.setRegion(null, null)
        }
    }

    companion object {
        private const val MY_LOG = "WorkPlaceVMMyLog"
    }
}
