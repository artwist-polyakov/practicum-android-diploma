package ru.practicum.android.diploma.filter.ui.fragment

import android.util.Log
import androidx.core.view.isVisible
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch
import ru.practicum.android.diploma.R
import ru.practicum.android.diploma.common.ui.BaseFragment
import ru.practicum.android.diploma.common.utils.setupTextChangeListener
import ru.practicum.android.diploma.databinding.FragmentWorkPlaceBinding
import ru.practicum.android.diploma.filter.ui.viewmodel.WorkPlaceViewModel
import ru.practicum.android.diploma.filter.ui.viewmodel.states.SearchRegionScreenState

@AndroidEntryPoint
class WorkPlaceFragment : BaseFragment<FragmentWorkPlaceBinding, WorkPlaceViewModel>(
    FragmentWorkPlaceBinding::inflate
) {
    override val viewModel by activityViewModels<WorkPlaceViewModel>()

    override fun initViews() {
        manageVisibilityButton()
        updateRegionClickable()
    }

    override fun subscribe(): Unit = with(binding) {
        filterFieldListeners()
        onCrossClicks()
        updateButtonBlockVisibility()

        viewLifecycleOwner.lifecycleScope.launch {
            viewModel.state.collect { state ->
                if (state is SearchRegionScreenState.Content) {
                    Log.i("WorkPlaceFragmentMyLog", "country = ${state.selectedCountry}")
                    tiRegion.setText(state.selectedRegion?.text)
                    tiCountry.setText(state.selectedCountry?.text)
                }
            }
        }

        ivArrowBack.setOnClickListener {
            findNavController().popBackStack()
        }

        tiCountry.setOnClickListener {
            findNavController().navigate(R.id.action_workPlaceFragment_to_countryFragment)
        }

        tiRegion.setOnClickListener {
            findNavController().navigate(R.id.action_workPlaceFragment_to_regionFragment)
        }
    }

    private fun filterFieldListeners() = with(binding) {
        tiCountry.setupTextChangeListener(tlCountry, ivArrowForwardCountry, requireContext()) {
            updateRegionClickable()
        }
        tiRegion.setupTextChangeListener(tlRegion, ivArrowForwardRegion, requireContext())
    }

    private fun manageVisibilityButton() = with(binding) {
        btnSelect.isVisible = tiCountry.text?.isNotEmpty() == true || tiRegion.text?.isNotEmpty() == true
    }

    private fun updateRegionClickable() = with(binding) {
        tiRegion.isClickable = tiCountry.text?.isNotEmpty() == true
    }

    private fun onCrossClicks() = with(binding) {
        ivArrowForwardCountry.setOnClickListener {
            tiCountry.text?.clear()
            viewModel.clearselectedCountry()
        }

        ivArrowForwardRegion.setOnClickListener {
            tiRegion.text?.clear()
            viewModel.clearselectedRegion()
        }
    }

    private fun updateButtonBlockVisibility() = with(binding) {
        btnSelect.isVisible = tiCountry.text.toString().isNotEmpty() || tiRegion.text.toString().isNotEmpty()
    }
}
