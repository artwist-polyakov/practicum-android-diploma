package ru.practicum.android.diploma.favorites.ui.fragments

import android.view.View.VISIBLE
import android.os.Bundle
import android.view.View.GONE
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch
import ru.practicum.android.diploma.R
import ru.practicum.android.diploma.common.ui.BaseFragment
import ru.practicum.android.diploma.common.utils.debounce
import ru.practicum.android.diploma.databinding.FragmentFavoriteBinding
import ru.practicum.android.diploma.favorites.ui.viewmodels.FavoriteViewModel
import ru.practicum.android.diploma.favorites.ui.viewmodels.states.FavoritesScreenState
import ru.practicum.android.diploma.search.domain.models.VacancyGeneral
import ru.practicum.android.diploma.search.ui.fragments.SearchFragment
import ru.practicum.android.diploma.search.ui.fragments.VacancyAdapter
import ru.practicum.android.diploma.vacancy.ui.VacancyFragment

@AndroidEntryPoint
class FavoriteFragment : BaseFragment<FragmentFavoriteBinding, FavoriteViewModel>(FragmentFavoriteBinding::inflate) {
    override val viewModel by viewModels<FavoriteViewModel>()
    private var onVacancyClickDebounce: ((VacancyGeneral) -> Unit)? = null
    private val vacancyListAdapter = VacancyAdapter { data ->
        onVacancyClickDebounce?.invoke(data)
    }

    override fun initViews() {
        binding.favoritesList.root.apply {
            layoutManager = LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false)
            adapter = vacancyListAdapter
        }
    }

    override fun subscribe() {
        onVacancyClickDebounce = debounce(
            CLICK_DEBOUNCE_DELAY,
            viewLifecycleOwner.lifecycleScope,
            false,
            false
        ) { data ->
            val bundle = Bundle().apply {
                putInt(VacancyFragment.ARG_ID, data.id)
            }
            findNavController().navigate(
                R.id.action_favoriteFragment_to_vacancyFragment,
                bundle
            )
        }

        viewLifecycleOwner.lifecycleScope.launch {
            viewModel.state.collect { state ->
                render(state)
            }
        }
    }

    private fun render(state: FavoritesScreenState) {
        when (state) {
            is FavoritesScreenState.Empty -> emptyFavorites(state)
            is FavoritesScreenState.Content -> showFavorites(state)
            is FavoritesScreenState.Error -> showError(state)
            is FavoritesScreenState.Loading -> isLoading()
        }
    }

    private fun emptyFavorites(state: FavoritesScreenState.Empty) {
        binding.ivFavoriteStateImage.visibility = VISIBLE
        binding.tvFavoriteStateText.visibility = VISIBLE
        binding.favoritesList.root.visibility = GONE
        binding.progressBar.visibility = GONE
        binding.ivFavoriteStateImage.setImageResource(state.image)
        binding.tvFavoriteStateText.setText(state.text)
    }

    private fun showFavorites(state: FavoritesScreenState.Content) {
        binding.ivFavoriteStateImage.visibility = GONE
        binding.tvFavoriteStateText.visibility = GONE
        binding.favoritesList.root.visibility = VISIBLE
        binding.progressBar.visibility = GONE
        vacancyListAdapter.setData(state.vacancies)
    }

    private fun showError(state: FavoritesScreenState.Error) {
        binding.ivFavoriteStateImage.visibility = VISIBLE
        binding.tvFavoriteStateText.visibility = VISIBLE
        binding.favoritesList.root.visibility = GONE
        binding.progressBar.visibility = GONE
        binding.ivFavoriteStateImage.setImageResource(state.image)
        binding.tvFavoriteStateText.setText(state.text)
    }

    private fun isLoading() {
        binding.ivFavoriteStateImage.visibility = GONE
        binding.tvFavoriteStateText.visibility = GONE
        binding.favoritesList.root.visibility = GONE
        binding.progressBar.visibility = VISIBLE
    }

    companion object {
        private const val CLICK_DEBOUNCE_DELAY = 500L
    }
}
