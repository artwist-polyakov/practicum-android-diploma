package ru.practicum.android.diploma.filter.domain.impl

import android.content.SharedPreferences
import com.google.gson.Gson
import ru.practicum.android.diploma.filter.data.dto.FilterSettingsDto
import ru.practicum.android.diploma.filter.domain.FilterSettingsRepository

private const val DATA_KEY = "FILTER_SETTINGS"

class FilterSettingsRepositoryImpl(
    private val sharedPreferences: SharedPreferences
) : FilterSettingsRepository {
    override fun getFilterSettings(): FilterSettingsDto {
        return sharedPreferences.getString(DATA_KEY, null)
            ?.let { text ->
                Gson().fromJson(text, FilterSettingsDto::class.java)
            }
            ?: FilterSettingsDto()
    }

    override fun saveFilterSettings(settings: FilterSettingsDto) {
        sharedPreferences.edit().putString(DATA_KEY, Gson().toJson(settings)).apply()
    }
}
