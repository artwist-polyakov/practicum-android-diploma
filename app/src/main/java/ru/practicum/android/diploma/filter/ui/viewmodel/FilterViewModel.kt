package ru.practicum.android.diploma.filter.ui.viewmodel

import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.launch
import ru.practicum.android.diploma.common.ui.BaseViewModel
import ru.practicum.android.diploma.filter.domain.FilterSettingsInteractor
import ru.practicum.android.diploma.filter.ui.viewmodel.states.FilterScreenState
import ru.practicum.android.diploma.filter.ui.viewmodel.states.FilterSettingsUIState
import ru.practicum.android.diploma.filter.ui.viewmodel.states.FilterViewModelInteraction
import javax.inject.Inject

@HiltViewModel
class FilterViewModel @Inject constructor(private val repository: FilterSettingsInteractor) : BaseViewModel() {
    private var filterSettingsUI: FilterSettingsUIState = FilterSettingsUIState()
    private var hadInitilized = false
    private var _state = MutableStateFlow<FilterScreenState>(FilterScreenState.Empty)
    open val state: MutableStateFlow<FilterScreenState>
        get() = _state

    init {
        checkState()
    }

    private fun firstLaunch() {
        if (!hadInitilized) {
            filterSettingsUI = FilterSettingsUIState(
                region = repository.getRegion().text,
                industry = repository.getIndustry().text,
                salary = repository.getSalary(),
                salaryOnly = repository.getWithSalaryOnly()
            )
            hadInitilized = true
            if (areSettingsSettled(filterSettingsUI)) {
                _state.value = FilterScreenState.Settled(
                    filterSettingsUI.region ?: "",
                    filterSettingsUI.industry ?: "",
                    filterSettingsUI.salary,
                    filterSettingsUI.salaryOnly,
                    true,
                    false
                )
            }
        }
    }


    private fun checkState() {
        firstLaunch()

        viewModelScope.launch {
            repository.getFilterUISettings()
                // нашедшему причину задвоенного возвращения состояния
                // префов от меня приз. А. Поляков.
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
                            isApplyEnabled
                        )
                        filterSettingsUI = it
                    }
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

    fun handleInteraction(kind: FilterViewModelInteraction) {
        when (kind) {
            FilterViewModelInteraction.clearSettings -> repository.resetSettings()
            FilterViewModelInteraction.saveSettings -> Unit
            is FilterViewModelInteraction.setSalary -> {
                repository.setSalary(kind.salary)
            }

            is FilterViewModelInteraction.setSalaryOnly -> {
                repository.setWithSalaryOnly(kind.onlySalary)
            }
        }
    }

}
