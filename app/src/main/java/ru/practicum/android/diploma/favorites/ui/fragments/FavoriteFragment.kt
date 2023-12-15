package ru.practicum.android.diploma.favorites.ui.fragments

import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch
import ru.practicum.android.diploma.common.ui.BaseFragment
import ru.practicum.android.diploma.databinding.FragmentFavoriteBinding
import ru.practicum.android.diploma.favorites.ui.viewmodels.FavoriteViewModel
import ru.practicum.android.diploma.favorites.ui.viewmodels.states.FavoritesScreenState

@AndroidEntryPoint
class FavoriteFragment : BaseFragment<FragmentFavoriteBinding, FavoriteViewModel>(FragmentFavoriteBinding::inflate) {
    override val viewModel by viewModels<FavoriteViewModel>()

    override fun initViews() {
        // Блок для инициализации ui

    }

    override fun subscribe() {
        // Блок для подписок (клики, viewModel)
        viewLifecycleOwner.lifecycleScope.launch {
            viewModel.state.collect { state ->
                render(state)
            }
        }
    }

    private fun render(state: FavoritesScreenState) {
        when (state) {
            is FavoritesScreenState.Empty -> {
                // Блок для отображения пустого экрана
            }
            is FavoritesScreenState.Content -> {
                // Блок для отображения контента
            }
            is FavoritesScreenState.Error -> {
                // Блок для отображения ошибки
            }
            is FavoritesScreenState.Loading -> {
                // Блок для отображения загрузки
            }
        }
    }
}
