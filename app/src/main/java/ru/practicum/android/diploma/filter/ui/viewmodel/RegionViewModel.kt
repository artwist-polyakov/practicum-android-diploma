package ru.practicum.android.diploma.filter.ui.viewmodel

import android.util.Log
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import ru.practicum.android.diploma.filter.domain.FilterSettingsInteractor
import ru.practicum.android.diploma.search.domain.api.SearchInteractor
import ru.practicum.android.diploma.search.ui.viewmodels.states.SearchScreenState
import javax.inject.Inject

@HiltViewModel
class RegionViewModel @Inject constructor(
    private val searchInteractor: SearchInteractor,
    private val filterInteractor: FilterSettingsInteractor
) : WorkPlaceViewModel(searchInteractor, filterInteractor) {

    fun setCountry(id: String, name: String) {
        Log.i("RegionViewModelMyLog", "setCountry id = $id, name = $name")
        updateStateWithCountry(id, name)
        filterInteractor.setRegion(id.toInt(), name)
    }

    fun setRegion(id: String, name: String) {
        updateStateWithRegion(id, name)
        filterInteractor.setRegion(id.toInt(), name)
    }
}
