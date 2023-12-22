package ru.practicum.android.diploma.filter.ui.fragment

import android.content.res.ColorStateList
import android.view.View.GONE
import android.view.View.VISIBLE
import androidx.core.content.ContextCompat
import androidx.core.view.isVisible
import androidx.core.widget.doOnTextChanged
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch
import ru.practicum.android.diploma.R
import ru.practicum.android.diploma.common.ui.BaseFragment
import ru.practicum.android.diploma.common.utils.debounce
import ru.practicum.android.diploma.common.utils.setupTextChangeListener
import ru.practicum.android.diploma.databinding.FragmentFilterBinding
import ru.practicum.android.diploma.filter.domain.models.FilterRegionValue
import ru.practicum.android.diploma.filter.ui.viewmodel.FilterViewModel
import ru.practicum.android.diploma.filter.ui.viewmodel.states.FilterScreenState
import ru.practicum.android.diploma.filter.ui.viewmodel.states.FilterViewModelInteraction

@AndroidEntryPoint
class FilterFragment : BaseFragment<FragmentFilterBinding, FilterViewModel>(FragmentFilterBinding::inflate) {
    override val viewModel by viewModels<FilterViewModel>()
    private var defaultHintColor: Int = 0
    private var activeHintColor: Int = 0
    private var filterRegionValue: FilterRegionValue? = null

    var salaryDebounce: (Int?) -> Unit = {}

    override fun initViews(): Unit = with(binding) {
        tiWorkPlace.setText(filterRegionValue?.text ?: "")

        defaultHintColor = ContextCompat.getColor(requireContext(), R.color.inputTextHint)
        activeHintColor = ContextCompat.getColor(requireContext(), R.color.blue)

        tiSalaryField.requestFocus()

    }

    override fun subscribe(): Unit = with(binding) {
        salaryDebounce = debounce<Int?>(
            DEBOUNCE_DELAY,
            viewLifecycleOwner.lifecycleScope,
            true
        ) {
            viewModel.handleInteraction(
                FilterViewModelInteraction.setSalary(it)
            )
        }

        inputListener()
        filterFieldListeners()
        arrowForwardListeners()

        viewLifecycleOwner.lifecycleScope.launch {
            viewModel.state.collect { state ->
                render(state)
            }
        }
        setupClicklisteners()
    }

    private fun setupClicklisteners() = with(binding) {
        ivArrowBack.setOnClickListener {
            findNavController().popBackStack()
        }

        llSalaryChecbox.setOnClickListener {
            checkboxHideWithSalary.isChecked = !checkboxHideWithSalary.isChecked
            viewModel.handleInteraction(
                FilterViewModelInteraction.setSalaryOnly(checkboxHideWithSalary.isChecked)
            )
        }

        checkboxHideWithSalary.setOnClickListener {
            viewModel.handleInteraction(
                FilterViewModelInteraction.setSalaryOnly(checkboxHideWithSalary.isChecked)
            )
        }

        ivInputButton.setOnClickListener {
            tiSalaryField.text?.clear()
        }

        tiWorkPlace.setOnClickListener {
            findNavController().navigate(R.id.action_filterFragment_to_workPlaceFragment)
        }

        tiIndustry.setOnClickListener {
            findNavController().navigate(R.id.action_filterFragment_to_industryFragment)
        }

        btnApply.setOnClickListener {
            viewModel.handleInteraction(
                FilterViewModelInteraction.saveSettings
            )
            findNavController().popBackStack()
        }

        btnReset.setOnClickListener {
            viewModel.handleInteraction(
                FilterViewModelInteraction.clearSettings
            )
            findNavController().popBackStack()
        }
    }

    override fun onResume() {
        super.onResume()
        viewLifecycleOwner.lifecycleScope.launch {
            viewModel.state.collect { state ->
                render(state)
            }
        }
    }


    private fun inputListener() = with(binding) {
        tiSalaryField.doOnTextChanged { text, _, _, _ ->
            ivInputButton.isVisible = text.toString().isNotEmpty()

//            updateButtonBlockVisibility()

            val hintColor = if (text.toString().isEmpty()) defaultHintColor else activeHintColor

            tlSalary.hintTextColor = ColorStateList.valueOf(hintColor)

            if (text.toString().startsWith("0") && text?.length!! > 1) {
                tiSalaryField.setText(text.toString().substring(1))
                tiSalaryField.setSelection(tiSalaryField.text?.length ?: 0)
            }
            salaryDebounce(if (text.toString().isNotEmpty()) text.toString().toInt() else null)

        }
    }

    // Слушатель полей фильтров отрасли и места работы
    private fun filterFieldListeners() = with(binding) {
        tiWorkPlace.setupTextChangeListener(tlWorkPlace, ivArrowForwardLocation, requireContext())
        tiIndustry.setupTextChangeListener(tlIndustry, ivArrowForwardIndustry, requireContext())
    }

    private fun arrowForwardListeners() = with(binding) {
        ivArrowForwardLocation.setOnClickListener {
            tiWorkPlace.text = null
            viewModel.handleInteraction(
                FilterViewModelInteraction.clearRegion
            )
        }
        ivArrowForwardIndustry.setOnClickListener {
            tiIndustry.text = null
            viewModel.handleInteraction(
                FilterViewModelInteraction.clearIndustry
            )
        }
    }

    private fun render(state: FilterScreenState) {
        when (state) {
            is FilterScreenState.Settled -> {
                binding.tiWorkPlace.setText(state.region)
                binding.tiIndustry.setText(state.industry)
                binding.tiSalaryField.setText((state.salary ?: "").toString())
                binding.checkboxHideWithSalary.isChecked = state.withSalaryOnly
                binding.llButtonBlock.visibility = VISIBLE
                binding.btnReset.visibility = VISIBLE
                binding.btnApply.visibility = if (state.isApplyButtonEnabled) VISIBLE else GONE
            }

            is FilterScreenState.Empty -> {
                binding.llButtonBlock.visibility = GONE
                binding.btnApply.visibility = GONE
                binding.btnReset.visibility = GONE
            }
        }
    }

    companion object {
        const val DEBOUNCE_DELAY = 1000L
    }
}
