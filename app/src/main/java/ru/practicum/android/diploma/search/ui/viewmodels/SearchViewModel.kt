package ru.practicum.android.diploma.search.ui.viewmodels

import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import ru.practicum.android.diploma.common.data.dto.Resource
import ru.practicum.android.diploma.common.domain.models.NetworkErrors
import ru.practicum.android.diploma.common.ui.BaseViewModel
import ru.practicum.android.diploma.common.utils.debounce
import ru.practicum.android.diploma.filter.domain.FilterSettingsInteractor
import ru.practicum.android.diploma.search.domain.api.SearchInteractor
import ru.practicum.android.diploma.search.domain.models.VacanciesSearchResult
import ru.practicum.android.diploma.search.domain.models.VacancyGeneral
import ru.practicum.android.diploma.search.ui.viewmodels.states.ErrorsSearchScreenStates
import ru.practicum.android.diploma.search.ui.viewmodels.states.SearchScreenState
import ru.practicum.android.diploma.search.ui.viewmodels.states.SearchSettingsState
import ru.practicum.android.diploma.search.ui.viewmodels.states.ViewModelInteractionState
import javax.inject.Inject

@HiltViewModel
open class SearchViewModel @Inject constructor(
    private val interactor: SearchInteractor,
    private val sharedPrefsInteractor: FilterSettingsInteractor
) : BaseViewModel() {
    private var searchSettings: SearchSettingsState = SearchSettingsState()
    protected val vacancies: MutableList<VacancyGeneral> = mutableListOf()
    private var showSnackBar: Boolean = false
    private var totalPages: Int = 0
    private var isLastUpdatePage = false
    private var _state = MutableStateFlow<SearchScreenState>(SearchScreenState.Error(ErrorsSearchScreenStates.EMPTY))
    open val state: StateFlow<SearchScreenState>
        get() = _state

    private val searchDebounce = debounce<SearchSettingsState>(
        SEARCH_DEBOUNCE_DELAY,
        viewModelScope,
        true
    ) {
        getVacancies(it)
    }

    init {
        viewModelScope.launch {
            searchSettings = searchSettings.copy(
                currentPage = 0,
                currentQuery = "",
                currentRegion = sharedPrefsInteractor.getRegion().id,
                currentIndustry = sharedPrefsInteractor.getIndustry().id,
                currentSalary = sharedPrefsInteractor.getSalary(),
                currentSalaryOnly = sharedPrefsInteractor.getWithSalaryOnly()
            )
            checkState()
        }
    }

    private fun checkState() {
        if (searchSettings.currentQuery.isEmpty()) {
            _state.value = SearchScreenState.Default(isFilterEnabled())
        }

        viewModelScope.launch {
            sharedPrefsInteractor.getSearchSettings()
                .collect { settings ->
                    if (checkSettingsToReset(settings)) {
                        handleSearchSettings(searchSettings)
                    }
                }
        }
    }

    @Suppress("ComplexCondition")
    private fun checkSettingsToReset(newSettings: SearchSettingsState): Boolean {
        if (newSettings.currentSalaryOnly != searchSettings.currentSalaryOnly ||
            newSettings.currentSalary != searchSettings.currentSalary ||
            newSettings.currentIndustry != searchSettings.currentIndustry ||
            newSettings.currentRegion != searchSettings.currentRegion
        ) {
            searchSettings = searchSettings.copy(
                currentPage = 0,
                currentSalaryOnly = newSettings.currentSalaryOnly,
                currentSalary = newSettings.currentSalary,
                currentIndustry = newSettings.currentIndustry,
                currentRegion = newSettings.currentRegion
            )
            return true
        } else {
            return false
        }
    }

    private fun chargeInteractorSearch(): suspend () -> Flow<Resource<VacanciesSearchResult>> = {
        interactor.searchVacancies(
            text = searchSettings.currentQuery,
            page = searchSettings.currentPage,
            area = searchSettings.currentRegion,
            industry = searchSettings.currentIndustry,
            salary = searchSettings.currentSalary,
            onlyWithSalary = searchSettings.currentSalaryOnly
        )
    }

    private fun handleSearchResponse(result: Resource<VacanciesSearchResult>) {
        when (result) {
            is Resource.Success -> {
                result.data?.let {
                    totalPages = it.totalPages
                }
                if (result.data?.vacancies.isNullOrEmpty()) {
                    showSnackBar = false
                    _state.value =
                        SearchScreenState.Error(ErrorsSearchScreenStates.NOT_FOUND, showSnackBar, isFilterEnabled())
                } else if (result.data!!.vacanciesFound > 0) {
                    showSnackBar = result.data.currentPage >= 0
                    _state.value = SearchScreenState.Content(
                        result.data.totalPages,
                        result.data.currentPage,
                        result.data.vacanciesFound,
                        vacancies.apply {
                            addAll(result.data.vacancies)
                        },
                        isFilterEnabled()
                    )
                }
            }

            is Resource.Error -> {
                if (isLastUpdatePage) {
                    searchSettings = searchSettings.copy(currentPage = searchSettings.currentPage - 1)
                    isLastUpdatePage = false
                }
                _state.value = SearchScreenState.Error(
                    when (result.error) {
                        NetworkErrors.NoInternet -> ErrorsSearchScreenStates.NO_INTERNET
                        else -> ErrorsSearchScreenStates.SERVER_ERROR
                    }, showSnackBar, isFilterEnabled()
                )
            }
        }
    }

    private fun getVacancies(settings: SearchSettingsState) {
        if (settings.currentQuery.isNotEmpty()) {
            _state.value = SearchScreenState.Loading(settings.currentPage, isFilterEnabled())
            viewModelScope.launch {
                chargeInteractorSearch().invoke()
                    .collect { result ->
                        handleSearchResponse(result)
                    }
            }
        }
    }

    private fun handleSearchSettings(searchSettings: SearchSettingsState) {
        if (searchSettings.currentQuery.isEmpty()) {
            _state.value = SearchScreenState.Default(isFilterEnabled())
        }

        if (searchSettings.currentPage > 0) {
            if (searchSettings.currentPage < totalPages) {
                getVacancies(searchSettings)
            }
        } else {
            vacancies.clear()
            searchDebounce(searchSettings)
        }
    }

    private fun isFilterEnabled(): Boolean =
        searchSettings.currentRegion != null ||
            searchSettings.currentIndustry != null ||
            searchSettings.currentSalary != null ||
            searchSettings.currentSalaryOnly

    fun handleInteraction(interaction: ViewModelInteractionState) {
        var newSearchSettings = searchSettings
        val t = isLastUpdatePage; isLastUpdatePage = false
        when (interaction) {
            is ViewModelInteractionState.setRegion -> {
                newSearchSettings = newSearchSettings.copy(currentPage = 0)
                newSearchSettings = newSearchSettings.copy(currentRegion = interaction.region)
            }

            is ViewModelInteractionState.setIndustry -> {
                newSearchSettings = newSearchSettings.copy(currentPage = 0)
                newSearchSettings = newSearchSettings.copy(currentIndustry = interaction.industry)
            }

            is ViewModelInteractionState.setSalary -> {
                newSearchSettings = newSearchSettings.copy(currentPage = 0)
                newSearchSettings = newSearchSettings.copy(currentSalary = interaction.salary)
            }

            is ViewModelInteractionState.setSalaryOnly -> {
                newSearchSettings = newSearchSettings.copy(currentPage = 0)
                newSearchSettings = newSearchSettings.copy(currentSalaryOnly = interaction.salaryOnly)
            }

            is ViewModelInteractionState.setQuery -> {
                if (interaction.query == searchSettings.currentQuery) {
                    isLastUpdatePage = t; return
                }
                showSnackBar = false
                newSearchSettings = newSearchSettings.copy(currentPage = 0)
                newSearchSettings = newSearchSettings.copy(currentQuery = interaction.query)
            }

            is ViewModelInteractionState.setPage -> {
                isLastUpdatePage = true
                newSearchSettings = newSearchSettings.copy(currentPage = interaction.page)
            }
        }
        if (newSearchSettings != searchSettings) {
            searchSettings = newSearchSettings
            handleSearchSettings(searchSettings)
        }
    }

    fun giveMyPageToReload(): Int =
        searchSettings.currentPage


    companion object {
        private const val SEARCH_DEBOUNCE_DELAY = 2000L
    }
}
