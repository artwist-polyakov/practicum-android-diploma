package ru.practicum.android.diploma.favorites.ui.fragments

import android.os.Bundle
import android.view.View.GONE
import android.view.View.VISIBLE
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch
import ru.practicum.android.diploma.R
import ru.practicum.android.diploma.common.ui.BaseFragment
import ru.practicum.android.diploma.common.utils.debounce
import ru.practicum.android.diploma.databinding.FragmentFavoriteBinding
import ru.practicum.android.diploma.favorites.ui.viewmodels.FavoriteViewModel
import ru.practicum.android.diploma.favorites.ui.viewmodels.states.FavoritesScreenState
import ru.practicum.android.diploma.search.domain.models.VacancyGeneral
import ru.practicum.android.diploma.search.ui.fragments.VacancyAdapter
import ru.practicum.android.diploma.vacancy.ui.VacancyFragment

@AndroidEntryPoint
class FavoriteFragment : BaseFragment<FragmentFavoriteBinding, FavoriteViewModel>(FragmentFavoriteBinding::inflate) {
    override val viewModel by viewModels<FavoriteViewModel>()
    private var onVacancyClickDebounce: ((VacancyGeneral) -> Unit)? = null
    private val vacancyListAdapter = VacancyAdapter { data ->
        onVacancyClickDebounce?.invoke(data)
    }

    override fun onResume() {
        super.onResume()
        viewModel.handleRequest()
    }

    override fun initViews() {
        binding.favoritesList.root.apply {
            layoutManager = LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false)
            adapter = vacancyListAdapter
            addOnScrollListener(
                object : RecyclerView.OnScrollListener() {
                    override fun onScrollStateChanged(
                        recyclerView: RecyclerView,
                        newState: Int
                    ) {
                        super.onScrollStateChanged(recyclerView, newState)
                        if (!recyclerView.canScrollVertically(1)) {
                            viewModel.nextPager()
                        }
                    }
                }
            )
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
        with(binding) {
            ivFavoriteStateImage.visibility = VISIBLE
            tvFavoriteStateText.visibility = VISIBLE
            favoritesList.root.visibility = GONE
            progressBar.visibility = GONE
            ivFavoriteStateImage.setImageResource(state.image)
            tvFavoriteStateText.setText(state.text)
        }
    }

    private fun showFavorites(state: FavoritesScreenState.Content) {
        with(binding) {
            ivFavoriteStateImage.visibility = GONE
            tvFavoriteStateText.visibility = GONE
            favoritesList.root.visibility = VISIBLE
            progressBar.visibility = GONE
        }
        vacancyListAdapter.setScrollLoadingEnabled(state.currentPage != state.totalPages - 1)
        vacancyListAdapter.setData(state.vacancies, state.currentPage)
    }

    private fun showError(state: FavoritesScreenState.Error) {
        with(binding) {
            ivFavoriteStateImage.visibility = VISIBLE
            tvFavoriteStateText.visibility = VISIBLE
            favoritesList.root.visibility = GONE
            progressBar.visibility = GONE
            ivFavoriteStateImage.setImageResource(state.image)
            tvFavoriteStateText.setText(state.text)
        }
    }

    private fun isLoading() {
        with(binding) {
            ivFavoriteStateImage.visibility = GONE
            tvFavoriteStateText.visibility = GONE
            favoritesList.root.visibility = GONE
            progressBar.visibility = VISIBLE
        }
    }

    companion object {
        private const val CLICK_DEBOUNCE_DELAY = 500L
    }
}
