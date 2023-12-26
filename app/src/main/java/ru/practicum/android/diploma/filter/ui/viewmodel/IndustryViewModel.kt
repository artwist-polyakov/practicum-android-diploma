package ru.practicum.android.diploma.filter.ui.viewmodel

import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import ru.practicum.android.diploma.common.data.dto.Resource
import ru.practicum.android.diploma.common.ui.BaseViewModel
import ru.practicum.android.diploma.filter.domain.FilterSettingsInteractor
import ru.practicum.android.diploma.filter.ui.viewmodel.states.IndustryScreenState
import ru.practicum.android.diploma.search.domain.api.SearchInteractor
import ru.practicum.android.diploma.search.domain.models.Industry
import javax.inject.Inject

@HiltViewModel
class IndustryViewModel @Inject constructor(
    private val searchInteractor: SearchInteractor,
    private val filterInteractor: FilterSettingsInteractor
) : BaseViewModel() {
    private var selectedIndustry: Industry? = null
    private var currentIndustry: String? = null
    private var _state = MutableStateFlow<IndustryScreenState>(IndustryScreenState.Default)
    open val state: StateFlow<IndustryScreenState>
        get() = _state

    fun setIndustry(data: Industry): Boolean {
        return currentIndustry?.let {
            if (data.id == currentIndustry) {
                false
            } else {
                selectedIndustry = data
                true
            }
        } ?: let {
            selectedIndustry = data
            true
        }
    }

    fun saveIndustryToPref() {
        selectedIndustry?.let { data ->
            viewModelScope.launch {
                filterInteractor.setIndustry(data.id, data.name)
                _state.value = IndustryScreenState.Hide
            }
        }
    }

    init {
        viewModelScope.launch {
            currentIndustry = filterInteractor.getIndustry().id
            searchInteractor.getIndustries()
                .collect { resource ->
                    when (resource) {
                        is Resource.Success -> {
                            resource.data?.let { list ->
                                _state.value = IndustryScreenState.Content(
                                    data = list,
                                    currentIndustry = currentIndustry
                                )
                            }
                        }

                        else -> {
                            _state.value = IndustryScreenState.Error
                        }
                    }
                }
        }
    }
}
