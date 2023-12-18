package ru.practicum.android.diploma.filter.ui.viewmodel

import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch
import ru.practicum.android.diploma.common.ui.BaseViewModel
import ru.practicum.android.diploma.filter.data.impl.FilterSettingsInteractorImpl
import ru.practicum.android.diploma.filter.ui.viewmodel.states.FilterScreenState
import ru.practicum.android.diploma.filter.ui.viewmodel.states.FilterSettingsUIState
import javax.inject.Inject

@HiltViewModel
class FilterViewModel @Inject constructor(private val repository: FilterSettingsInteractorImpl) : BaseViewModel() {
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
                .collect {
                    val isResetEnabled = areSettingsSettled(it)
                    val isApplyEnabled = areSettingsChanged(it)
                    _state.value = FilterScreenState.Settled(
                        it.region ?: "",
                        it.industry ?: "",
                        it.salary,
                        it.salaryOnly,
                        isResetEnabled,
                        isApplyEnabled)
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
