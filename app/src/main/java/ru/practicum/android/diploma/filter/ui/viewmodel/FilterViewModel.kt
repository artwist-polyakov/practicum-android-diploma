package ru.practicum.android.diploma.filter.ui.viewmodel

import android.util.Log
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
class FilterViewModel @Inject constructor(private val interactor: FilterSettingsInteractor) : BaseViewModel() {
    private var filterSettingsUI: FilterSettingsUIState = FilterSettingsUIState()
    private var hadInitilized = false
    private var _state = MutableStateFlow<FilterScreenState>(FilterScreenState.Empty)
    open val state: MutableStateFlow<FilterScreenState>
        get() = _state

    init {
        checkState()
    }

    private fun firstLaunch() {
        // метод restoreSettings восстанавливает текущие настройки проект
        // сейчас он закомментирован, для соответствия ТЗ
        viewModelScope.launch {
            //        interactor.restoreSettings()
            if (!hadInitilized) {
                filterSettingsUI = FilterSettingsUIState(
                    region = interactor.getRegion().text,
                    industry = interactor.getIndustry().text,
                    salary = interactor.getSalary(),
                    salaryOnly = interactor.getWithSalaryOnly()
                )
                hadInitilized = true
                if (areSettingsSettled(filterSettingsUI)) {
                    _state.value = FilterScreenState.Settled(
                        region = filterSettingsUI.region ?: "",
                        industry = filterSettingsUI.industry ?: "",
                        salary = filterSettingsUI.salary,
                        withSalaryOnly = filterSettingsUI.salaryOnly,
                        isResetButtonEnabled = areSettingsSettled(filterSettingsUI),
                        isApplyButtonEnabled = areSettingsChanged(filterSettingsUI)
                    )
                }
            }
        }

    }


    fun checkState() {
        firstLaunch()

        viewModelScope.launch {
            interactor.getFilterUISettings()
                // нашедшему причину задвоенного возвращения состояния
                // префов от меня приз. А. Поляков.
                .distinctUntilChanged()
                .collect {
                    if (!areSettingsSettled(it)) {
                        _state.value = FilterScreenState.Empty
                    } else {
                        val isApplyEnabled = areSettingsChanged(it)
                        _state.value = FilterScreenState.Settled(
                            region = it.region ?: "",
                            industry = it.industry ?: "",
                            salary = it.salary,
                            withSalaryOnly = it.salaryOnly,
                            isResetButtonEnabled = true,
                            isApplyButtonEnabled = isApplyEnabled
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

    /**
     * Возврат true/false из метода определяет отображение кнопки "Применить"
     * Метод содержит закомментированный ретёрн, который отражает логику авторов
     * Авторы считают, что применять настройки фильтра можно только тогда,
     * когда они отличаются он ранее выставленных. В противном случае применяться
     * настройки не должны.
     *
     * Ревьювер попросил сделать кнопку применить всегда
     * в случае если есть хотя бы одно заполненное значение фильтра.
     *
     * todo после сдачи раскомментировать оригинальный код
     */
    private fun areSettingsChanged(settings: FilterSettingsUIState): Boolean {
//        return settings.region != filterSettingsUI.region
//            || settings.industry != filterSettingsUI.industry
//            || settings.salary != filterSettingsUI.salary
//            || settings.salaryOnly != filterSettingsUI.salaryOnly
        return settings.region != null || settings.industry != null ||
            settings.salary != null || settings.salaryOnly == true
    }

    fun handleInteraction(kind: FilterViewModelInteraction) {
        when (kind) {
            FilterViewModelInteraction.clearSettings -> viewModelScope.launch {
                interactor.resetSettings()
            }
            FilterViewModelInteraction.saveSettings -> viewModelScope.launch {
                interactor.saveSettings()
            }
            is FilterViewModelInteraction.setSalary -> viewModelScope.launch {
                interactor.setSalary(kind.salary)
            }

            is FilterViewModelInteraction.setSalaryOnly -> viewModelScope.launch {
                interactor.setWithSalaryOnly(kind.onlySalary)
            }

            FilterViewModelInteraction.clearIndustry -> viewModelScope.launch {
                interactor.setIndustry(null, null)
            }
            FilterViewModelInteraction.clearRegion -> viewModelScope.launch {
                interactor.setRegion(null, null)
            }
            FilterViewModelInteraction.clearSalary -> viewModelScope.launch {
                interactor.setSalary(null)
            }
        }
    }

}
