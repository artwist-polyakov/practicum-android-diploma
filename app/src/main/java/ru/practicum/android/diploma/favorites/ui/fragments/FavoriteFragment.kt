package ru.practicum.android.diploma.favorites.ui.fragments

import android.app.AlertDialog
import android.content.Context
import android.content.DialogInterface
import android.os.Bundle
import android.view.MenuInflater
import android.view.View
import android.view.View.GONE
import android.view.View.VISIBLE
import android.widget.PopupMenu
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch
import ru.practicum.android.diploma.R
import ru.practicum.android.diploma.common.ui.BaseFragment
import ru.practicum.android.diploma.common.ui.MainActivityBlur
import ru.practicum.android.diploma.common.utils.applyBlurEffect
import ru.practicum.android.diploma.common.utils.clearBlurEffect
import ru.practicum.android.diploma.common.utils.debounce
import ru.practicum.android.diploma.common.utils.vibrateShot
import ru.practicum.android.diploma.databinding.FragmentFavoriteBinding
import ru.practicum.android.diploma.favorites.ui.viewmodels.FavoriteViewModel
import ru.practicum.android.diploma.favorites.ui.viewmodels.states.FavoritesScreenState
import ru.practicum.android.diploma.search.domain.models.VacancyGeneral
import ru.practicum.android.diploma.search.ui.fragments.VacancyAdapter
import ru.practicum.android.diploma.vacancy.ui.VacancyFragment

@AndroidEntryPoint
class FavoriteFragment : BaseFragment<FragmentFavoriteBinding, FavoriteViewModel>(FragmentFavoriteBinding::inflate) {
    override val viewModel by viewModels<FavoriteViewModel>()
    private var mainActivityBlur: MainActivityBlur? = null
    private var onVacancyClickDebounce: ((VacancyGeneral) -> Unit)? = null
    private val vacancyListAdapter = VacancyAdapter(
        { data ->
            onVacancyClickDebounce?.invoke(data)
        },
        { data, view ->
            suggestTrackDeleting(data, view)
        }
    )

    override fun onResume() {
        super.onResume()
        viewModel.handleRequest()
    }

    override fun onAttach(context: Context) {
        super.onAttach(context)
        if (context is MainActivityBlur) {
            mainActivityBlur = context
        }
    }

    override fun onDetach() {
        super.onDetach()
        mainActivityBlur = null
    }

    override fun initViews() {
        binding.favoritesList.root.apply {
            layoutManager = LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false)
            adapter = vacancyListAdapter
            addOnScrollListener(object : RecyclerView.OnScrollListener() {
                override fun onScrollStateChanged(
                    recyclerView: RecyclerView,
                    newState: Int
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
            is FavoritesScreenState.Content -> {
                showFavorites(state)
                vacancyListAdapter.setData(state.vacancies, state.currentPage)
            }

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

    private fun suggestTrackDeleting(vacancy: VacancyGeneral, anchorView: View) {
        requireContext().vibrateShot(100L)
        val popupMenu = PopupMenu(
            context,
            anchorView,
            0,
            0,
            R.style.PopupMenu
        )
        val inflater: MenuInflater = popupMenu.menuInflater
        inflater.inflate(R.menu.delete_from_favorite, popupMenu.menu)
        popupMenu.setOnMenuItemClickListener { menuItem ->
            when (menuItem.itemId) {
                R.id.action_delete -> {
                    applyBlur(true)
                    val alertDialogBuilder: AlertDialog.Builder =
                        AlertDialog.Builder(requireContext(), R.style.AlertDialog)
                    alertDialogBuilder.setTitle(getString(R.string.deleting_confirmation))
                    alertDialogBuilder.setMessage(getString(R.string.remove_vacancy, vacancy.title))
                    alertDialogBuilder.setPositiveButton(getString(R.string.yes)) { _: DialogInterface, _: Int ->
                        viewModel.deleteFromFavorites(vacancy)
                        applyBlur(false)
                    }
                    alertDialogBuilder.setNegativeButton(getString(R.string.no)) { _: DialogInterface, _: Int ->
                        applyBlur(false)
                    }
                    val alertDialog: AlertDialog = alertDialogBuilder.create()
                    alertDialog.show()
                    alertDialog.getButton(AlertDialog.BUTTON_NEGATIVE)
                        .setTextColor(resources.getColor(R.color.blackUniversal, null))
                    alertDialog.getButton(AlertDialog.BUTTON_POSITIVE)
                        .setTextColor(resources.getColor(R.color.red, null))
                    true
                }

                R.id.action_share -> {
                    viewModel.shareVacancy(vacancy)
                    true
                }

                else -> false
            }
        }
        popupMenu.show()
    }

    private fun applyBlur(blurOn: Boolean) = with(binding) {
        if (blurOn) {
            llHeader.applyBlurEffect()
            mainActivityBlur?.applyBlurEffect()
            scrollView.applyBlurEffect()
        } else {
            llHeader.clearBlurEffect()
            mainActivityBlur?.clearBlurEffect()
            scrollView.clearBlurEffect()
        }
    }

    companion object {
        private const val CLICK_DEBOUNCE_DELAY_500MS = 500L
    }
}
