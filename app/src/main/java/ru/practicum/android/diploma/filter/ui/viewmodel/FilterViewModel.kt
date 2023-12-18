package ru.practicum.android.diploma.filter.ui.viewmodel

import android.util.Log
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.launch
import ru.practicum.android.diploma.common.ui.BaseViewModel
import ru.practicum.android.diploma.filter.data.impl.FilterSettingsInteractorImpl
import ru.practicum.android.diploma.filter.domain.FilterSettingsInteractor
import ru.practicum.android.diploma.filter.ui.viewmodel.states.FilterScreenState
import ru.practicum.android.diploma.filter.ui.viewmodel.states.FilterSettingsUIState
import javax.inject.Inject

@HiltViewModel
class FilterViewModel @Inject constructor(private val repository: FilterSettingsInteractor) : BaseViewModel() {
    private var filterSettingsUI: FilterSettingsUIState = FilterSettingsUIState()
    private var _state = MutableStateFlow<FilterScreenState>(FilterScreenState.Empty)
    open val state: MutableStateFlow<FilterScreenState>
        get() = _state

    init {
        checkState()
    }


    private fun checkState() {
        viewModelScope.launch {
            repository.getFilterUISettings()
                .distinctUntilChanged()
                .collect {
                    if (!areSettingsSettled(it)) {
                        _state.value = FilterScreenState.Empty
                    } else {
                        val isApplyEnabled = areSettingsChanged(it)
                        _state.value = FilterScreenState.Settled(
                            it.region ?: "",
                            it.industry ?: "",
                            it.salary,
                            it.salaryOnly,
                            true,
                            isApplyEnabled)
                    }
                    Log.d("FilterViewModel", "${_state.value}")
                }
        }
    }

    private fun areSettingsSettled(settings: FilterSettingsUIState): Boolean {
        return settings.region != null
            || settings.industry != null
            || settings.salary != null
            || settings.salaryOnly
    }

    private fun areSettingsChanged(settings: FilterSettingsUIState): Boolean {
        return settings.region != filterSettingsUI.region
            || settings.industry != filterSettingsUI.industry
            || settings.salary != filterSettingsUI.salary
            || settings.salaryOnly != filterSettingsUI.salaryOnly
    }

}
