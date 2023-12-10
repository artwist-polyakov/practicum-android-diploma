package ru.practicum.android.diploma.common.di

import android.content.Context
import androidx.room.Room
import com.google.gson.Gson
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import ru.practicum.android.diploma.common.data.db.AppDatabase
import ru.practicum.android.diploma.common.data.network.HHService
import ru.practicum.android.diploma.common.data.network.NetworkClient
import ru.practicum.android.diploma.common.data.network.RetrofitNetworkClient
import ru.practicum.android.diploma.favorites.domain.api.FavoritesDBConverter
import ru.practicum.android.diploma.favorites.domain.impl.FavoritesDBConverterImpl
import ru.practicum.android.diploma.search.domain.api.SearchResultConverter
import ru.practicum.android.diploma.search.domain.impl.SearchResultConverterImpl
import ru.practicum.android.diploma.vacancy.domain.api.SingleVacancyConverter
import ru.practicum.android.diploma.vacancy.domain.impl.SingleVacancyConverterImpl
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
class DataModule {
    @Provides
    @Singleton
    fun provideHHService(): HHService = Retrofit.Builder()
        .baseUrl("https://api.hh.ru/")
        .addConverterFactory(GsonConverterFactory.create())
        .build()
        .create(HHService::class.java)

    @Provides
    @Singleton
    fun provideGson(): Gson = Gson()

    @Provides
    @Singleton
    fun providesNetworkClient(hhService: HHService, @ApplicationContext context: Context): NetworkClient =
        RetrofitNetworkClient(hhService, context)

    @Provides
    @Singleton
    fun providesAppDatabase(@ApplicationContext context: Context): AppDatabase =
        Room.databaseBuilder(context, AppDatabase::class.java, "database.db")
            .addMigrations()
            .build()

    @Provides
    fun providesSearchResultConverter(): SearchResultConverter = SearchResultConverterImpl()

    @Provides
    fun providesFavoritesDBConverter(): FavoritesDBConverter = FavoritesDBConverterImpl(gsonService = provideGson())

    @Provides
    fun providesSingleVacancyConverter(): SingleVacancyConverter = SingleVacancyConverterImpl(gson = provideGson())
}
