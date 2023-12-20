package ru.practicum.android.diploma.filter.domain

import android.content.SharedPreferences
import kotlinx.coroutines.flow.Flow
import ru.practicum.android.diploma.filter.data.dto.FilterSettingsDto

interface FilterSettingsRepository : SharedPreferences.OnSharedPreferenceChangeListener {
    fun getFilterSettings(): FilterSettingsDto
    fun settingsFlow(): Flow<FilterSettingsDto>
    fun saveFilterSettings(settings: FilterSettingsDto)
    fun destroy()
}
