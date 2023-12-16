package ru.practicum.android.diploma.filter.ui.viewmodel.states

import ru.practicum.android.diploma.filter.domain.models.FilterRegionValue
import ru.practicum.android.diploma.search.domain.models.SingleTreeElement
import ru.practicum.android.diploma.search.ui.viewmodels.states.ErrorsSearchScreenStates

sealed class SearchRegionScreenState {
    data object Loading : SearchRegionScreenState()
    data class Error(
        val error: ErrorsSearchScreenStates
    ) : SearchRegionScreenState()

    data class Content(
        val regions: List<SingleTreeElement>,
        val selectedCountry: FilterRegionValue? = null,
        val selectedRegion: FilterRegionValue? = null
    ) : SearchRegionScreenState()
}
