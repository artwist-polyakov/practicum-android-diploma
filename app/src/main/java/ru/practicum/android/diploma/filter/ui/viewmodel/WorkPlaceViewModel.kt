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

    private var country: FilterRegionValue? = null
    private var region: FilterRegionValue? = null
    private var countryList: regions? = null
    private var regionList: regions? = null

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

    private fun provideResponse(result: Resource<regions>) {
        when (result) {
            is Resource.Success -> {
                if (result.data.isNullOrEmpty()) {
                    _state.value = SearchRegionScreenState.Error(ErrorsSearchScreenStates.FAIL_FETCH_REGIONS)
                } else {
                    if (regionList == null) {
                        val currentState = _state.value as? SearchRegionScreenState.Content
                        countryList = getCountries(result.data)
                        regionList = result.data

                        _state.value = currentState?.copy(regions = regionList!!, countries = countryList!!)
                            ?: SearchRegionScreenState.Content(
                                regions = regionList!!,
                                countries = countryList!!
                            )
                    }
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
    fun updateStateWithCountry(id: String, name: String, region: FilterRegionValue? = null) {
        viewModelScope.launch {
            val currentState = _state.value
            if (currentState is SearchRegionScreenState.Content && country != FilterRegionValue(id.toInt(), name)) {
                _state.value = currentState.copy(
                    selectedCountry = FilterRegionValue(id.toInt(), name),
                    selectedRegion = region
                )
                country = FilterRegionValue(id.toInt(), name)
            }
        }
    }

    /**
     * Метод обновляет значение в state selectedRegion.
     * Если country еще не обозначен, то метод находит первого родителя и присваивает его поля в country
     **/
    fun updateStateWithRegion(id: String, name: String) {
        viewModelScope.launch {
            val currentState = _state.value
            if (currentState is SearchRegionScreenState.Content) {
                val newRegion = FilterRegionValue(id.toInt(), name)
                _state.value = currentState.copy(selectedRegion = newRegion)
                region = newRegion

                if (currentState.selectedCountry == null) {
                    val parent: SingleTreeElement? =
                        getCountryFromRegion(currentState.regions, region!!.id!!.toString())
                    updateStateWithCountry(parent?.id.toString(), parent?.name ?: "", newRegion)
                }
            }
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
            region = null
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

    fun unpackRegions(area: regions): regions {
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

    fun getFilterArea(): FilterRegionValue? {
        Log.i(MY_LOG, "region = $region \ncountry = $country")
        return when {
            region != null -> FilterRegionValue(
                id = region!!.id,
                text = if (country != null) "${country!!.text}, ${region!!.text}" else region!!.text
            )

            country != null -> FilterRegionValue(id = country!!.id, text = country!!.text)
            else -> null
        }
    }

    fun getCountries(area: regions): regions = area.filter { it.parent == null }


    fun saveRegionToPrefs(region: FilterRegionValue) {
        filterInteractor.setRegion(region.id, region.text)
    }

    fun filterRegions(regionList: List<SingleTreeElement>, input: String): List<SingleTreeElement> {
        val lowerCaseInput = input.lowercase().trim()
        return regionList.filter {
            it.name.lowercase().contains(lowerCaseInput)
        }
    }

    companion object {
        private const val MY_LOG = "WorkPlaceVMMyLog"
    }
}
