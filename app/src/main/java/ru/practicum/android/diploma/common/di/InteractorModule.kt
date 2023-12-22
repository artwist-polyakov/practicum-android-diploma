package ru.practicum.android.diploma.common.di

import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ViewModelComponent
import ru.practicum.android.diploma.common.data.db.AppDatabase
import ru.practicum.android.diploma.favorites.domain.api.FavoritesDBConverter
import ru.practicum.android.diploma.favorites.domain.api.FavoritesDBInteractor
import ru.practicum.android.diploma.favorites.domain.impl.FavoritesDBInteractorImpl
import ru.practicum.android.diploma.filter.data.impl.FilterSettingsInteractorImpl
import ru.practicum.android.diploma.filter.domain.FilterSettingsInteractor
import ru.practicum.android.diploma.filter.domain.FilterSettingsRepository
import ru.practicum.android.diploma.search.data.HHSearchRepository
import ru.practicum.android.diploma.search.domain.api.SearchInteractor
import ru.practicum.android.diploma.search.domain.api.SearchResultConverter
import ru.practicum.android.diploma.search.domain.impl.SearchInteractorImpl
import ru.practicum.android.diploma.vacancy.domain.api.ExternalNavigator
import ru.practicum.android.diploma.vacancy.domain.api.SingleVacancyConverter
import ru.practicum.android.diploma.vacancy.domain.api.SingleVacancyInteractor
import ru.practicum.android.diploma.vacancy.domain.impl.SingleVacancyInteractorImpl

@Module
@InstallIn(ViewModelComponent::class)
class InteractorModule {
    @Provides
    fun providesSearchInteractor(
        repository: HHSearchRepository,
        converter: SearchResultConverter
    ): SearchInteractor = SearchInteractorImpl(repository, converter)

    @Provides
    fun providesFavoritesDbInteractor(
        database: AppDatabase,
        converter: FavoritesDBConverter,
        repository: HHSearchRepository,
        converterSingle: SingleVacancyConverter
    ): FavoritesDBInteractor = FavoritesDBInteractorImpl(database, converter, repository, converterSingle)

    @Provides
    fun providesSingleVacancyInteractor(
        repository: HHSearchRepository,
        converter: SingleVacancyConverter,
        database: AppDatabase,
        externalNavigator: ExternalNavigator
    ): SingleVacancyInteractor = SingleVacancyInteractorImpl(repository, converter, database, externalNavigator)

    @Provides
    fun provideFilterSettingInteractor(
        @TemporarySettingsRepositoryImpl repository: FilterSettingsRepository,
        @SearchSettingsRepository secondRepository: FilterSettingsRepository
    ): FilterSettingsInteractor = FilterSettingsInteractorImpl(repository, secondRepository)
}
