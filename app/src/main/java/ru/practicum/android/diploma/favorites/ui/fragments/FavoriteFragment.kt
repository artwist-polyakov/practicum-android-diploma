package ru.practicum.android.diploma.favorites.ui.fragments

import android.app.AlertDialog
import android.content.Context
import android.content.DialogInterface
import android.os.Bundle
import android.view.View
import android.view.View.GONE
import android.view.View.VISIBLE
import androidx.core.content.ContextCompat
import androidx.core.content.res.ResourcesCompat
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.skydoves.powermenu.MenuAnimation
import com.skydoves.powermenu.PowerMenu
import com.skydoves.powermenu.PowerMenuItem
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
    private var onVacancyClickDebounce: ((VacancyGeneral) -> Unit)? = null
    private val vacancyListAdapter = VacancyAdapter(
        clickListener = { data ->
            onVacancyClickDebounce?.invoke(data)
        },
        onLongClickListener = ::suggestTrackDeleting
    )

    override fun onResume() {
        super.onResume()
        viewModel.loadVacancies()
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
                        viewModel.loadVacancies()
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
        requireContext().vibrateShot(VIBRATE_DURATION_100MS)
        val powerMenu = buildMenu()

        powerMenu.setOnMenuItemClickListener { position, _ ->
            when (position) {
                0 -> {
                    viewModel.shareVacancy(vacancy)
                    powerMenu.dismiss()
                }

                1 -> {
                    buildDialog(vacancy)
                    powerMenu.dismiss()
                }
            }
        }

        powerMenu.showAsAnchorLeftBottom(anchorView)
    }

    private fun buildMenu(): PowerMenu {
        val typeface = ResourcesCompat.getFont(requireContext(), R.font.ys_display_medium)!!

        return PowerMenu.Builder(requireContext())
            .setLifecycleOwner(viewLifecycleOwner)
            .addItem(PowerMenuItem(getString(R.string.share_item), iconRes = R.drawable.ic_blue_sharing_24px))
            .addItem(PowerMenuItem(getString(R.string.delete_item), iconRes = R.drawable.ic_trash_basket))
            .setAnimation(MenuAnimation.SHOWUP_TOP_LEFT)
            .setMenuRadius(MENU_CORNER_RADIUS)
            .setShowBackground(true)
            .setMenuShadow(MENU_CORNER_RADIUS)
            .setMenuColor(ContextCompat.getColor(requireContext(), R.color.formsBackground))
            .setTextColor(ContextCompat.getColor(requireContext(), R.color.htmlText))
            .setTextSize(FONT_SIZE)
            .setTextTypeface(typeface)
            .setBackgroundColor(ContextCompat.getColor(requireContext(), R.color.transparent))
            .setSize(dpToPx(MENU_WIDTH_DP, requireContext()), dpToPx(MENU_HEIGHT_DP, requireContext()))
            .setPadding(MENU_PADDING)
            .build()
    }

    private fun dpToPx(dp: Int, context: Context): Int {
        val density = context.resources.displayMetrics.density
        return (dp * density).toInt()
    }

    private fun buildDialog(vacancy: VacancyGeneral) {
        applyBlur(true)
        val alertDialogBuilder: AlertDialog.Builder =
            AlertDialog.Builder(requireContext(), R.style.AlertDialog)
        alertDialogBuilder.setTitle(getString(R.string.deleting_confirmation))
            .setMessage(getString(R.string.remove_vacancy, vacancy.title))
            .setPositiveButton(getString(R.string.yes)) { _: DialogInterface, _: Int ->
                viewModel.deleteFromFavorites(vacancy)
                applyBlur(false)
            }
            .setNegativeButton(getString(R.string.no)) { _: DialogInterface, _: Int ->
                applyBlur(false)
            }
        val alertDialog: AlertDialog = alertDialogBuilder.create()
        alertDialog.setOnCancelListener {
            applyBlur(false)
        }
        alertDialog.show()
        alertDialog.getButton(AlertDialog.BUTTON_NEGATIVE)
            .setTextColor(resources.getColor(R.color.blackUniversal, null))
        alertDialog.getButton(AlertDialog.BUTTON_POSITIVE)
            .setTextColor(resources.getColor(R.color.red, null))
    }

    private fun applyBlur(blurOn: Boolean) = with(binding) {
        if (blurOn) {
            llHeader.applyBlurEffect()
            (requireActivity() as? MainActivityBlur)?.applyBlurEffect()
            scrollView.applyBlurEffect()
        } else {
            llHeader.clearBlurEffect()
            (requireActivity() as? MainActivityBlur)?.clearBlurEffect()
            scrollView.clearBlurEffect()
        }
    }

    companion object {
        private const val CLICK_DEBOUNCE_DELAY_500MS = 500L
        private const val VIBRATE_DURATION_100MS = 100L
        private const val MENU_CORNER_RADIUS = 48f
        private const val FONT_SIZE = 18
        private const val MENU_WIDTH_DP = 200
        private const val MENU_HEIGHT_DP = 140
        private const val MENU_PADDING = 16
    }
}
