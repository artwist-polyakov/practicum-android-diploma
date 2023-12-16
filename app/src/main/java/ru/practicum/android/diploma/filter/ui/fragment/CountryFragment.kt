package ru.practicum.android.diploma.filter.ui.fragment

import android.util.Log
import android.view.View
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch
import ru.practicum.android.diploma.common.ui.BaseFragment
import ru.practicum.android.diploma.common.utils.debounce
import ru.practicum.android.diploma.databinding.FragmentCountryBinding
import ru.practicum.android.diploma.filter.ui.adapters.LocationAdapter
import ru.practicum.android.diploma.filter.ui.viewmodel.WorkPlaceViewModel
import ru.practicum.android.diploma.filter.ui.viewmodel.states.SearchRegionScreenState
import ru.practicum.android.diploma.search.domain.models.SingleTreeElement
import ru.practicum.android.diploma.search.ui.viewmodels.states.ErrorsSearchScreenStates

@AndroidEntryPoint
class CountryFragment : BaseFragment<FragmentCountryBinding, WorkPlaceViewModel>(FragmentCountryBinding::inflate) {
    override val viewModel by activityViewModels<WorkPlaceViewModel>()
    private var onRegionClickDebounce: ((SingleTreeElement) -> Unit)? = null
    private val locationAdapter = LocationAdapter() { data ->
        onRegionClickDebounce?.invoke(data)
    }

    override fun initViews() {
        binding.locationList.root.apply {
            layoutManager = LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false)
            adapter = locationAdapter
        }
    }

    override fun subscribe() {
        with(binding) {
            ivArrowBack.setOnClickListener {
                findNavController().popBackStack()
            }
        }

        onRegionClickDebounce = debounce(
            CLICK_DEBOUNCE_DELAY_500MS,
            viewLifecycleOwner.lifecycleScope,
            false
        ) { data ->
            viewModel.updateStateWithCountry(data.id, data.name)
            Log.i("CountryFragmentMyLog", "country = ${data.name}  id = ${data.id}")
            findNavController().popBackStack()
        }

        viewLifecycleOwner.lifecycleScope.launch {
            viewModel.getAreas()
            viewModel.state.collect { state ->
                renderState(state)
            }
        }
    }

    private fun renderState(state: SearchRegionScreenState) {
        when (state) {
            is SearchRegionScreenState.Content -> {
                showData(state.regions)
                Log.i("CountryFragmentMyLog", "state selectedCountry = ${state.selectedCountry}")
            }

            is SearchRegionScreenState.Error -> {
                showEror(state.error)
            }

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

    private fun showEror(error: ErrorsSearchScreenStates) {
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
