package ru.practicum.android.diploma.filter.data.impl

import android.content.SharedPreferences
import com.google.gson.Gson
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.receiveAsFlow
import ru.practicum.android.diploma.filter.data.dto.FilterSettingsDto
import ru.practicum.android.diploma.filter.domain.FilterSettingsRepository

class FilterSettingsRepositoryImpl(
    private val sharedPreferences: SharedPreferences,
    private val key: String
) : FilterSettingsRepository {

    // Этот канал будет использоваться для оповещения о изменениях настроек
    private val settingsUpdateChannel =
        Channel<FilterSettingsDto>(Channel.CONFLATED)

    init {
        sharedPreferences.registerOnSharedPreferenceChangeListener(this)
    }

    override fun getFilterSettings(): FilterSettingsDto {
        return sharedPreferences.getString(key, null)
            ?.let { text ->
                Gson().fromJson(text, FilterSettingsDto::class.java)
            }
            ?: FilterSettingsDto()
    }

    override fun saveFilterSettings(settings: FilterSettingsDto) {
        sharedPreferences.edit().putString(key, Gson().toJson(settings)).apply()
        settingsUpdateChannel.trySend(settings).isSuccess
    }

    override fun settingsFlow(): Flow<FilterSettingsDto> = settingsUpdateChannel.receiveAsFlow()

    override fun onSharedPreferenceChanged(
        sharedPreferences: SharedPreferences,
        key: String?
    ) {
        if (this.key == key) {
            // Настройки были изменены, отправляем новое состояние в канал
            val settings = getFilterSettings()
            settingsUpdateChannel.trySend(settings).isSuccess
        }
    }

    // Вызывать в месте очистки ресурсов, чтобы избежать утечек памяти
    override fun destroy() {
        sharedPreferences.unregisterOnSharedPreferenceChangeListener(this)
        settingsUpdateChannel.close()
    }
}
