package ru.practicum.android.diploma.filter.ui.fragment

import android.os.Bundle
import androidx.core.view.isVisible
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import ru.practicum.android.diploma.R
import ru.practicum.android.diploma.common.ui.BaseFragment
import ru.practicum.android.diploma.common.utils.setupTextChangeListener
import ru.practicum.android.diploma.databinding.FragmentWorkPlaceBinding
import ru.practicum.android.diploma.filter.ui.viewmodel.WorkPlaceViewModel

class WorkPlaceFragment : BaseFragment<FragmentWorkPlaceBinding, WorkPlaceViewModel>(
    FragmentWorkPlaceBinding::inflate
) {
    override val viewModel by viewModels<WorkPlaceViewModel>()

    override fun initViews() {
        manageVisibilityButton()
        updateRegionClickable()
    }

    override fun subscribe(): Unit = with(binding) {
        filterFieldListeners()

        ivArrowBack.setOnClickListener {
            findNavController().popBackStack()
        }

        tiCountry.setOnClickListener {
            findNavController().navigate(R.id.action_workPlaceFragment_to_countryFragment)
        }

        tiRegion.setOnClickListener {
            val bundle = Bundle().apply {
                putInt(COUNTRY_KEY, 1)
            }
            findNavController().navigate(R.id.action_workPlaceFragment_to_regionFragment, bundle)
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

    companion object {
        private const val COUNTRY_KEY = "country"
    }
}
