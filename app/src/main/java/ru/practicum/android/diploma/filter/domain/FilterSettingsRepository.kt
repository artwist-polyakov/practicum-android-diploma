package ru.practicum.android.diploma.filter.domain

import android.content.SharedPreferences
import kotlinx.coroutines.flow.Flow
import ru.practicum.android.diploma.filter.data.dto.FilterSettingsDto

interface FilterSettingsRepository : SharedPreferences.OnSharedPreferenceChangeListener {
    suspend fun getFilterSettings(): FilterSettingsDto
    fun settingsFlow(): Flow<FilterSettingsDto>
    suspend fun saveFilterSettings(settings: FilterSettingsDto)
}
