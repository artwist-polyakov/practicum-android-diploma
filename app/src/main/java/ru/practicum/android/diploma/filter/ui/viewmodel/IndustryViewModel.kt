package ru.practicum.android.diploma.filter.ui.viewmodel

import android.util.Log
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import ru.practicum.android.diploma.common.data.dto.Resource
import ru.practicum.android.diploma.common.ui.BaseViewModel
import ru.practicum.android.diploma.filter.ui.viewmodel.states.IndustryScreenState
import ru.practicum.android.diploma.search.domain.api.SearchInteractor
import javax.inject.Inject

@HiltViewModel
class IndustryViewModel @Inject constructor(
    private val interactor: SearchInteractor
) : BaseViewModel() {
    private var _state = MutableStateFlow<IndustryScreenState>(IndustryScreenState.Default)
    open val state: StateFlow<IndustryScreenState>
        get() = _state

    init {
        viewModelScope.launch {
            interactor.getIndustries()
                .collect { resource ->
                    when (resource) {
                        is Resource.Success -> {
                            resource.data?.let {list ->
                                _state.value = IndustryScreenState.Content(
                                    data = list
                                )
                            }
                        }

                        else -> {
                        }
                    }
                }
        }
    }
}
