package ru.practicum.android.diploma.filter.ui.viewmodel

import android.util.Log
import dagger.hilt.android.lifecycle.HiltViewModel
import ru.practicum.android.diploma.filter.domain.FilterSettingsInteractor
import ru.practicum.android.diploma.search.domain.api.SearchInteractor
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
