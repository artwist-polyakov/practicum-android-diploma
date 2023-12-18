package ru.practicum.android.diploma.filter.ui.fragment

import android.view.View
import androidx.core.widget.doOnTextChanged
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch
import ru.practicum.android.diploma.R
import ru.practicum.android.diploma.common.ui.BaseFragment
import ru.practicum.android.diploma.common.utils.debounce
import ru.practicum.android.diploma.databinding.FragmentRegionBinding
import ru.practicum.android.diploma.filter.ui.adapters.LocationAdapter
import ru.practicum.android.diploma.filter.ui.viewmodel.WorkPlaceViewModel
import ru.practicum.android.diploma.filter.ui.viewmodel.states.SearchRegionScreenState
import ru.practicum.android.diploma.search.domain.models.SingleTreeElement
import ru.practicum.android.diploma.search.ui.viewmodels.states.ErrorsSearchScreenStates

@AndroidEntryPoint
class RegionFragment : BaseFragment<FragmentRegionBinding, WorkPlaceViewModel>(FragmentRegionBinding::inflate) {
    override val viewModel by activityViewModels<WorkPlaceViewModel>()
    private var onRegionClickDebounce: ((SingleTreeElement) -> Unit)? = null
    private val locationAdapter = LocationAdapter { data ->
        onRegionClickDebounce?.invoke(data)
    }

    override fun initViews() {
        binding.locationList.root.apply {
            layoutManager = LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false)
            adapter = locationAdapter
        }
    }

    override fun subscribe() {
        inputListener()

        with(binding) {
            onRegionClickDebounce = debounce(
                CLICK_DEBOUNCE_DELAY_500MS,
                viewLifecycleOwner.lifecycleScope,
                useLastParam = false,
                actionWithDelay = false
            ) { data ->
                viewModel.updateStateWithRegion(data.id, data.name)
                findNavController().popBackStack()
            }

            viewLifecycleOwner.lifecycleScope.launch {
                viewModel.getRegions()
                viewModel.state.collect { state ->
                    renderState(state)
                }
            }

            ivSearchFieldButton.setOnClickListener {
                if (tiSearchField.text.toString().isNotEmpty()) tiSearchField.text?.clear()
            }

            ivArrowBack.setOnClickListener {
                findNavController().popBackStack()
            }
        }
    }

    private fun inputListener() {
        binding.tiSearchField.doOnTextChanged { text, _, _, _ ->
            val inputText = text.toString()
            if (inputText.isNotEmpty()) {
                processNonEmptyInput(inputText)
            } else {
                processEmptyInput()
            }
        }
    }

    private fun processNonEmptyInput(inputText: String) {
        binding.ivSearchFieldButton.setImageResource(R.drawable.close_24px)

        val currentState = viewModel.state.value
        if (currentState is SearchRegionScreenState.Content) {
            val filteredRegions = viewModel.filterRegions(
                viewModel.unpackRegions(currentState.regions[0].children!!),
                inputText
            )
            if (filteredRegions.isEmpty()) {
                showError(ErrorsSearchScreenStates.NO_REGION)
            } else {
                showData(filteredRegions)
            }
        }
    }

    private fun processEmptyInput() {
        binding.ivSearchFieldButton.setImageResource(R.drawable.search_24px)

        val currentState = viewModel.state.value
        if (currentState is SearchRegionScreenState.Content) {
            showData(viewModel.unpackRegions(currentState.regions[0].children!!))
        }
    }

    private fun renderState(state: SearchRegionScreenState) {
        when (state) {
            is SearchRegionScreenState.Content -> showData(viewModel.unpackRegions(state.regions[0].children!!))

            is SearchRegionScreenState.Error -> showError(state.error)

            else -> showProgressBar()
        }
    }

    private fun showData(regions: List<SingleTreeElement>) {
        showData()
        locationAdapter.submitList(regions)
    }

    private fun showData() {
        with(binding) {
            tvErrorMessage.visibility = View.GONE
            locationList.root.visibility = View.VISIBLE
            progressBar.visibility = View.GONE
        }
    }

    private fun showError(error: ErrorsSearchScreenStates) {
        with(binding) {
            locationList.root.visibility = View.GONE
            progressBar.visibility = View.GONE

            tvErrorMessage.visibility = View.VISIBLE
            tvErrorMessage.text = getString(error.messageResource)
            tvErrorMessage.setCompoundDrawablesWithIntrinsicBounds(0, error.imageResource, 0, 0)
        }
    }

    private fun showProgressBar() {
        with(binding) {
            tvErrorMessage.visibility = View.GONE
            progressBar.visibility = View.VISIBLE
            locationList.root.visibility = View.GONE
        }
    }

    companion object {
        private const val CLICK_DEBOUNCE_DELAY_500MS = 500L
    }
}
