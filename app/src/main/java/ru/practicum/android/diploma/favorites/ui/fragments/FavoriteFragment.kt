package ru.practicum.android.diploma.favorites.ui.fragments

import android.app.AlertDialog
import android.content.DialogInterface
import android.graphics.Color
import android.os.Bundle
import android.view.View.GONE
import android.view.View.VISIBLE
import android.widget.PopupMenu
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.dialog.MaterialAlertDialogBuilder
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
    private val vacancyListAdapter = VacancyAdapter({ data -> onVacancyClickDebounce?.invoke(data) },
        { data -> if (suggestTrackDeleting(data)) viewModel.deleteFromFavorites(data) else true })

    override fun onResume() {
        super.onResume()
        viewModel.handleRequest()
    }

    override fun initViews() {
        binding.favoritesList.root.apply {
            layoutManager = LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false)
            adapter = vacancyListAdapter
            addOnScrollListener(object : RecyclerView.OnScrollListener() {
                override fun onScrollStateChanged(
                    recyclerView: RecyclerView, newState: Int
                ) {
                    super.onScrollStateChanged(recyclerView, newState)
                    if (!recyclerView.canScrollVertically(1)) {
                        viewModel.handleRequest()
                    }
                }
            })
        }
    }

    override fun subscribe() {
        onVacancyClickDebounce = debounce(
            CLICK_DEBOUNCE_DELAY_500MS,
            viewLifecycleOwner.lifecycleScope,
            useLastParam = false,
            actionWithDelay = false
        ) { data ->
            val bundle = Bundle().apply {
                putInt(VacancyFragment.ARG_ID, data.id)
            }
            findNavController().navigate(
                R.id.action_favoriteFragment_to_vacancyFragment, bundle
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

    private fun suggestTrackDeleting(vacancy: VacancyGeneral): Boolean {
        var decision = false
        val vacancyName = vacancy.title
        val popupMenu = PopupMenu(context, view)
        popupMenu.inflate(R.menu.delete_from_favorite)
        popupMenu.setOnMenuItemClickListener { menuItem ->
            when (menuItem.itemId) {
                R.id.action_delete -> {
                    decision = true
                    val alertDialogBuilder: AlertDialog.Builder = AlertDialog.Builder(requireContext())
                    alertDialogBuilder.setTitle("Подтверждение удаления")
                    alertDialogBuilder.setMessage("Вы уверены, что хотите удалить вакансию $vacancyName?")
                    alertDialogBuilder.setPositiveButton("Да") { _: DialogInterface, _: Int ->
                        decision = true
                    }
                    alertDialogBuilder.setNegativeButton("Нет") { _: DialogInterface, _: Int ->
                        decision = false
                    }
                    val alertDialog: AlertDialog = alertDialogBuilder.create()
                    alertDialog.show()
                    alertDialog.getButton(AlertDialog.BUTTON_NEGATIVE)
                        .setTextColor(resources.getColor(R.color.blackDay, null))
                    alertDialog.getButton(AlertDialog.BUTTON_POSITIVE)
                        .setTextColor(resources.getColor(R.color.blackDay, null))
                    return@setOnMenuItemClickListener true
                }

                else -> {
                    decision = false
                    return@setOnMenuItemClickListener false
                }
            }
        }
        popupMenu.show()
        return decision
    }

    companion object {
        private const val CLICK_DEBOUNCE_DELAY_500MS = 500L
    }
}
