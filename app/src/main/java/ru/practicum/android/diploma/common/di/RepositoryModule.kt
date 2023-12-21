package ru.practicum.android.diploma.common.di

import android.content.SharedPreferences
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import ru.practicum.android.diploma.common.data.network.NetworkClient
import ru.practicum.android.diploma.filter.data.impl.FilterSettingsRepositoryImpl
import ru.practicum.android.diploma.filter.domain.FilterSettingsRepository
import ru.practicum.android.diploma.search.data.HHSearchRepository
import ru.practicum.android.diploma.search.data.HHSearchRepositoryImpl
import javax.inject.Qualifier
import javax.inject.Singleton

@Qualifier
@Retention(AnnotationRetention.BINARY)
annotation class TemporarySettingsRepositoryImpl

@Qualifier
@Retention(AnnotationRetention.BINARY)
annotation class SearchSettingsRepository

@Module
@InstallIn(SingletonComponent::class)
class RepositoryModule {
    @Provides
    @Singleton
    fun providesSearchRepository(networkClient: NetworkClient): HHSearchRepository =
        HHSearchRepositoryImpl(networkClient)

    @Provides
    @Singleton
    @TemporarySettingsRepositoryImpl
    fun provideFilterRepository(sharedPreferences: SharedPreferences): FilterSettingsRepository =
        FilterSettingsRepositoryImpl(sharedPreferences, TEMP_DATA_KEY)

    @Provides
    @Singleton
    @SearchSettingsRepository
    fun provideFinalFilterRepository(sharedPreferences: SharedPreferences): FilterSettingsRepository =
        FilterSettingsRepositoryImpl(sharedPreferences, FINAL_DATA_KEY)

    companion object {
        private const val TEMP_DATA_KEY = "filter_settings"
        private const val FINAL_DATA_KEY = "final_filter_settings"
    }
}
