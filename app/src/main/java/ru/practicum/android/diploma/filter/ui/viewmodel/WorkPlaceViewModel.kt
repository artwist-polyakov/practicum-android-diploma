package ru.practicum.android.diploma.filter.ui.viewmodel

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

typealias regions = List<SingleTreeElement>

/**
 * WorkPlaceViewModel является Shared ViewModel для WorkPlaceFragment, CountryFragment и RegionFragment
 */
@HiltViewModel
class WorkPlaceViewModel @Inject constructor(
    private val searchInteractor: SearchInteractor,
    private val filterInteractor: FilterSettingsInteractor
) : BaseViewModel() {
    private var _state = MutableStateFlow<SearchRegionScreenState>(SearchRegionScreenState.Loading)
    val state: StateFlow<SearchRegionScreenState>
        get() = _state

    private var country: FilterRegionValue = FilterRegionValue(null, null)
    private var region: FilterRegionValue = FilterRegionValue(null, null)
    private var countryList: regions = emptyList()
    private var regionList: regions = emptyList()

    fun getAreas() {
        viewModelScope.launch {
            searchInteractor.getAreas().collect { result ->
                provideResponse(result)
            }
        }
    }

    fun getRegions() {
        viewModelScope.launch {
            searchInteractor.getAreas(country.id).collect { result ->
                provideResponse(result)
            }
        }
    }

    private fun provideResponse(result: Resource<regions>) {
        when (result) {
            is Resource.Success -> {
                if (result.data.isNullOrEmpty()) {
                    _state.value = SearchRegionScreenState.Error(ErrorsSearchScreenStates.FAIL_FETCH_REGIONS)
                } else {
                    if (regionList.isEmpty()) {
                        countryList = getCountries(result.data)
                        regionList = result.data
                        val currentState = _state.value as? SearchRegionScreenState.Content
                        _state.value = currentState?.copy(regions = regionList, countries = countryList)
                            ?: SearchRegionScreenState.Content(
                                regions = unpackRegions(regionList).filter { it.parent != null },
                                countries = countryList
                            )
                    }
                }
            }

            is Resource.Error -> {
                if (regionList.isEmpty()) {
                    _state.value = SearchRegionScreenState.Error(
                        when (result.error) {
                            NetworkErrors.NoInternet -> ErrorsSearchScreenStates.NO_INTERNET
                            else -> ErrorsSearchScreenStates.SERVER_ERROR
                        }
                    )
                } else {
                    val currentState = _state.value as? SearchRegionScreenState.Content
                    _state.value = currentState?.copy(
                        regions = unpackRegions(regionList).filter { it.parent != null },
                        countries = countryList
                    ) ?: SearchRegionScreenState.Content(
                        regions = unpackRegions(regionList).filter { it.parent != null },
                        countries = countryList
                    )
                }
            }
        }
    }

    fun updateStateWithRegion(id: String, name: String) {
        viewModelScope.launch {
            region = FilterRegionValue(id.toInt(), name)
            val parentRegion = getCountryFromRegion(regionList, region.id.toString())
            country = FilterRegionValue(parentRegion?.id?.toInt(), parentRegion?.name)
            updateState(country, region)
        }
    }

    fun updateStateWithCountry(id: String, name: String, region: FilterRegionValue? = null) {
        country = FilterRegionValue(id.toInt(), name)
        updateState(country, region)
    }

    private fun updateState(selectedCountry: FilterRegionValue?, selectedRegion: FilterRegionValue?) {
        viewModelScope.launch {
            val currentState = _state.value
            if (currentState is SearchRegionScreenState.Content) {
                val countryIndex = countryList.indexOfFirst { it.id == selectedCountry?.id.toString() }
                val childrenList = regionList[countryIndex].children
                val regions = unpackRegions(childrenList!!)
                _state.value = currentState.copy(
                    selectedCountry = selectedCountry,
                    selectedRegion = selectedRegion,
                    regions = regions
                )
            }
        }
    }

    fun clearselectedCountry() {
        viewModelScope.launch {
            val currentState = _state.value
            if (currentState is SearchRegionScreenState.Content) {
                _state.value = currentState.copy(
                    selectedCountry = null,
                    regions = unpackRegions(regionList).filter { it.parent != null }
                )
            }
            country = FilterRegionValue(null, null)
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
            region = FilterRegionValue(null, null)
        }
    }

    private fun getCountryFromRegion(areas: regions, childId: String): SingleTreeElement? {
        val flatList = unpackRegions(areas)
        var currentElement = flatList.find { it.id == childId }

        while (currentElement?.parent != null) {
            currentElement = flatList.find { it.id == currentElement!!.parent.toString() }
        }
        return currentElement
    }

    fun getFilterArea(): FilterRegionValue? {
        return when {
            region.id != null -> FilterRegionValue(
                id = region.id,
                text = if (country.id != null) "${country.text}, ${region.text}" else region.text
            )

            country.id != null -> country
            else -> null
        }
    }

    fun filterRegions(regionList: List<SingleTreeElement>, input: String): List<SingleTreeElement> {
        val lowerCaseInput = input.lowercase().trim()
        return regionList.filter {
            it.name.lowercase().contains(lowerCaseInput)
        }
    }

    fun saveRegionToPrefs(region: FilterRegionValue) {
        filterInteractor.setRegion(region.id, region.text)
    }

    fun processNonEmptyInput(inputText: String) {
        viewModelScope.launch {
            val currentState = _state.value
            if (currentState is SearchRegionScreenState.Content) {
                val filteredRegions = filterRegions(currentState.regions, inputText)
                if (filteredRegions.isNotEmpty()) {
                    _state.value = currentState.copy(regions = filteredRegions)
                }
            }
        }
    }

    fun processEmptyInput() {
        viewModelScope.launch {
            val currentState = _state.value
            if (currentState is SearchRegionScreenState.Content) {
                _state.value =
                    currentState.copy(regions = unpackRegions(regionList).filter { it.parent != null })
            }
        }
    }

    private fun getCountries(area: regions): regions = area.filter { it.parent == null }

    private fun unpackRegions(area: regions): regions {
        val unpackedList = mutableListOf<SingleTreeElement>()

        fun unpack(list: regions) {
            for (element in list) {
                unpackedList.add(element)
                element.children?.let { unpack(it) }
            }
        }

        unpack(area)
        return unpackedList
    }
}
