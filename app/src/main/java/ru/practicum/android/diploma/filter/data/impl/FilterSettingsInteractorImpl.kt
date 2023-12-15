package ru.practicum.android.diploma.filter.data.impl

import ru.practicum.android.diploma.filter.data.dto.FilterIndustryDto
import ru.practicum.android.diploma.filter.data.dto.FilterRegionDto
import ru.practicum.android.diploma.filter.data.dto.FilterSettingsDto
import ru.practicum.android.diploma.filter.domain.FilterSettingsInteractor
import ru.practicum.android.diploma.filter.domain.FilterSettingsRepository
import ru.practicum.android.diploma.filter.domain.models.FilterIndustryValue
import ru.practicum.android.diploma.filter.domain.models.FilterRegionValue
import ru.practicum.android.diploma.search.ui.viewmodels.states.SearchSettingsState

class FilterSettingsInteractorImpl(
    private val repository: FilterSettingsRepository
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
    }

    override fun getSearchSettings(): SearchSettingsState {
        return SearchSettingsState(
            currentRegion = getRegion().id,
            currentSalary = getSalary(),
            currentIndustry = getIndustry().id,
            currentSalaryOnly = getWithSalaryOnly(),
        )
    }
}
