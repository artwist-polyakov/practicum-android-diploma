package ru.practicum.android.diploma.common.di

import android.content.SharedPreferences
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import ru.practicum.android.diploma.common.data.network.NetworkClient
import ru.practicum.android.diploma.filter.data.impl.FilterSettingsRepositoryImpl
import ru.practicum.android.diploma.filter.data.impl.FinalFilterRepositoryImpl
import ru.practicum.android.diploma.filter.domain.FilterSettingsRepository
import ru.practicum.android.diploma.search.data.HHSearchRepository
import ru.practicum.android.diploma.search.data.HHSearchRepositoryImpl
import javax.inject.Qualifier
import javax.inject.Singleton

@Qualifier
@Retention(AnnotationRetention.BINARY)
annotation class FilterSettingsRepositoryImpl1

@Qualifier
@Retention(AnnotationRetention.BINARY)
annotation class FinalFilterRepositoryImpl2

@Module
@InstallIn(SingletonComponent::class)
class RepositoryModule {
    @Provides
    @Singleton
    fun providesSearchRepository(networkClient: NetworkClient): HHSearchRepository =
        HHSearchRepositoryImpl(networkClient)

    @Provides
    @Singleton
    @FilterSettingsRepositoryImpl1
    fun provideFilterRepository(sharedPreferences: SharedPreferences): FilterSettingsRepository =
        FilterSettingsRepositoryImpl(sharedPreferences)

    @Provides
    @Singleton
    @FinalFilterRepositoryImpl2
    fun provideFinalFilterRepository(sharedPreferences: SharedPreferences): FilterSettingsRepository =
        FinalFilterRepositoryImpl(sharedPreferences)
}
