package ru.practicum.android.diploma.filter.ui.viewmodel

import dagger.hilt.android.lifecycle.HiltViewModel
import ru.practicum.android.diploma.common.ui.BaseViewModel
import ru.practicum.android.diploma.search.domain.api.SearchInteractor
import javax.inject.Inject

@HiltViewModel
class RegionViewModel @Inject constructor(private val interactor: SearchInteractor) : BaseViewModel()
