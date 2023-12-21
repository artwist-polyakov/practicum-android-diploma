package ru.practicum.android.diploma.filter.data.impl

import kotlinx.coroutines.flow.conflate
import kotlinx.coroutines.flow.map
import ru.practicum.android.diploma.filter.data.dto.FilterIndustryDto
import ru.practicum.android.diploma.filter.data.dto.FilterRegionDto
import ru.practicum.android.diploma.filter.data.dto.FilterSettingsDto
import ru.practicum.android.diploma.filter.domain.FilterSettingsInteractor
import ru.practicum.android.diploma.filter.domain.FilterSettingsRepository
import ru.practicum.android.diploma.filter.domain.models.FilterIndustryValue
import ru.practicum.android.diploma.filter.domain.models.FilterRegionValue
import ru.practicum.android.diploma.filter.ui.viewmodel.states.FilterSettingsUIState
import ru.practicum.android.diploma.search.ui.viewmodels.states.SearchSettingsState

class FilterSettingsInteractorImpl(
    private val repository: FilterSettingsRepository,
    private val finalRepository: FilterSettingsRepository
) : FilterSettingsInteractor {

    override fun setRegion(id: Int?, name: String?) {
        val current = repository.getFilterSettings()
        repository.saveFilterSettings(
            settings = FilterSettingsDto(
                region = FilterRegionDto(
                    id = id,
                    name = name
                ),
                industry = current.industry,
                salary = current.salary,
                withSalaryOnly = current.withSalaryOnly
            )
        )
    }

    override fun getRegion(): FilterRegionValue {
        val data = repository.getFilterSettings().region
        return FilterRegionValue(
            id = data.id,
            text = data.name
        )
    }

    override fun setIndustry(id: String?, name: String?) {
        val current = repository.getFilterSettings()
        repository.saveFilterSettings(
            settings = FilterSettingsDto(
                region = current.region,
                industry = FilterIndustryDto(
                    id = id,
                    name = name
                ),
                salary = current.salary,
                withSalaryOnly = current.withSalaryOnly
            )
        )
    }

    override fun getIndustry(): FilterIndustryValue {
        val data = repository.getFilterSettings().industry
        return FilterIndustryValue(
            id = data.id,
            text = data.name
        )
    }

    override fun setSalary(value: Int?) {
        val current = repository.getFilterSettings()
        repository.saveFilterSettings(
            settings = FilterSettingsDto(
                region = current.region,
                industry = current.industry,
                salary = value,
                withSalaryOnly = current.withSalaryOnly
            )
        )
    }

    override fun getSalary(): Int? {
        return repository.getFilterSettings().salary
    }

    override fun setWithSalaryOnly(state: Boolean) {
        val current = repository.getFilterSettings()
        repository.saveFilterSettings(
            settings = FilterSettingsDto(
                region = current.region,
                industry = current.industry,
                salary = current.salary,
                withSalaryOnly = state
            )
        )
    }

    override fun getWithSalaryOnly(): Boolean {
        return repository.getFilterSettings().withSalaryOnly
    }

    override fun resetSettings() {
        repository.saveFilterSettings(
            settings = FilterSettingsDto()
        )
        finalRepository.saveFilterSettings(
            settings = FilterSettingsDto()
        )
    }

    override fun saveSettings() {
        finalRepository.saveFilterSettings(
            repository.getFilterSettings()
        )
    }

    /**
     * Позволяет восстановить настройки, которые применены в текущем фильтре
     */
    override fun restoreSettings() {
        repository.saveFilterSettings(
            finalRepository.getFilterSettings()
        )
    }

    override fun getSearchSettings() = finalRepository.settingsFlow().map { dto ->
        // преобразуем DTO в SETTINGS-стэйт
        SearchSettingsState(
            currentPage = -1,
            currentQuery = "",
            currentRegion = dto.region?.id,
            currentSalary = dto.salary,
            currentIndustry = dto.industry?.id,
            currentSalaryOnly = dto.withSalaryOnly,
        )
    }.conflate()

    override fun getFilterUISettings() = repository.settingsFlow().map { dto ->
        // преобразуем DTO в UI-стэйт
        FilterSettingsUIState(
            region = dto.region?.name,
            salary = dto.salary,
            industry = dto.industry?.name,
            salaryOnly = dto.withSalaryOnly,
        )
    }.conflate()


}
