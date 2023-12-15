package ru.practicum.android.diploma.favorites.ui.viewmodels.states

import ru.practicum.android.diploma.R
import ru.practicum.android.diploma.search.domain.models.VacancyGeneral

sealed class FavoritesScreenState {

    data class Loading(
        val isBottomIndicator: Boolean = false
    ) : FavoritesScreenState()

    data class Empty(
        val image: Int = R.drawable.image_andy,
        val text: Int = R.string.empty_list
    ) : FavoritesScreenState()

    data class Content(
        val totalPages: Int,
        val currentPage: Int,
        val totalVacancies: Int,
        val vacancies: List<VacancyGeneral>
    ) : FavoritesScreenState()

    data class Error(
        val image: Int = R.drawable.image_meme_cat,
        val text: Int = R.string.no_vacancies
    ) : FavoritesScreenState()
}
