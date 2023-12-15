package ru.practicum.android.diploma.filter.domain

import ru.practicum.android.diploma.filter.data.dto.FilterSettingsDto

interface FilterSettingsRepository {
    fun getFilterSettings(): FilterSettingsDto

    fun saveFilterSettings(settings: FilterSettingsDto)
}
