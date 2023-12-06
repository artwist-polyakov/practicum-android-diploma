package ru.practicum.android.diploma.common.di

import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ViewModelComponent
import ru.practicum.android.diploma.vacancy.domain.impl.VacancyInteractor
import ru.practicum.android.diploma.vacancy.domain.impl.VacancyInteractorImpl

@Module
@InstallIn(ViewModelComponent::class)
class InteractorModule {
    @Provides
    fun provideVacancyInteractor(): VacancyInteractor = VacancyInteractorImpl()
}
