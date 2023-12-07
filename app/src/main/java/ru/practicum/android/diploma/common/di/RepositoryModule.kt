package ru.practicum.android.diploma.common.di

import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import ru.practicum.android.diploma.common.data.network.NetworkClient
import ru.practicum.android.diploma.search.data.network.HHSearchRepository
import ru.practicum.android.diploma.search.data.network.HHSearchRepositoryImpl
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
class RepositoryModule {
    @Provides
    @Singleton
    fun providesSearchRepository(networkClient: NetworkClient): HHSearchRepository =
        HHSearchRepositoryImpl(networkClient)
}
