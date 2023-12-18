package ru.practicum.android.diploma.filter.ui.fragment

import android.content.res.ColorStateList
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
import ru.practicum.android.diploma.common.utils.setupTextChangeListener
import ru.practicum.android.diploma.databinding.FragmentFilterBinding
import ru.practicum.android.diploma.filter.domain.models.FilterRegionValue
import ru.practicum.android.diploma.filter.ui.viewmodel.FilterViewModel
import ru.practicum.android.diploma.filter.ui.viewmodel.states.FilterScreenState

@AndroidEntryPoint
class FilterFragment : BaseFragment<FragmentFilterBinding, FilterViewModel>(FragmentFilterBinding::inflate) {
    override val viewModel by viewModels<FilterViewModel>()
    private var defaultHintColor: Int = 0
    private var activeHintColor: Int = 0
    private var filterRegionValue: FilterRegionValue? = null
    override fun initViews(): Unit = with(binding) {
        tiWorkPlace.setText(filterRegionValue?.text ?: "")

        defaultHintColor = ContextCompat.getColor(requireContext(), R.color.inputTextHint)
        activeHintColor = ContextCompat.getColor(requireContext(), R.color.blue)

        tiSalaryField.requestFocus()
    }

    override fun subscribe(): Unit = with(binding) {
        inputListener()
        filterFieldListeners()
        arrowForwardListeners()

        viewLifecycleOwner.lifecycleScope.launch {
            viewModel.state.collect { state ->
                render(state)
            }
        }

        ivArrowBack.setOnClickListener {
            findNavController().popBackStack()
        }

        llSalaryChecbox.setOnClickListener {
            checkboxHideWithSalary.isChecked = !checkboxHideWithSalary.isChecked
            updateButtonBlockVisibility()
        }

        checkboxHideWithSalary.setOnClickListener {
            updateButtonBlockVisibility()
        }

        ivInputButton.setOnClickListener {
            tiSalaryField.text?.clear()
        }

        tiWorkPlace.setOnClickListener {
            findNavController().navigate(R.id.action_filterFragment_to_workPlaceFragment)
        }

        tiIndustry.setOnClickListener { Unit }

        btnApply.setOnClickListener { Unit }

        btnReset.setOnClickListener {
            resetFilter()
        }
    }

    private fun inputListener() = with(binding) {
        tiSalaryField.doOnTextChanged { text, _, _, _ ->
            ivInputButton.isVisible = text.toString().isNotEmpty()

            updateButtonBlockVisibility()

            val hintColor = if (text.toString().isEmpty()) defaultHintColor else activeHintColor

            tlSalary.hintTextColor = ColorStateList.valueOf(hintColor)

            if (text.toString().startsWith("0") && text?.length!! > 1) {
                tiSalaryField.setText(text.toString().substring(1))
                tiSalaryField.setSelection(tiSalaryField.text?.length ?: 0)
            }
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
        }
        ivArrowForwardIndustry.setOnClickListener {
            tiIndustry.text = null
        }
    }

    private fun resetFilter() = with(binding) {
        tiWorkPlace.text = null
        tiIndustry.text = null
        tiSalaryField.text = null
        checkboxHideWithSalary.isChecked = false
        llButtonBlock.isVisible = false
    }

    private fun updateButtonBlockVisibility() = with(binding) {
        llButtonBlock.isVisible = checkboxHideWithSalary.isChecked || tiSalaryField.text.toString().isNotEmpty()
    }

    private fun render(state: FilterScreenState) {
        when (state) {
            is FilterScreenState.Settled -> {
                binding.tiWorkPlace.setText(state.region)
                binding.tiIndustry.setText(state.industry)
                binding.tiSalaryField.setText((state.salary ?: "").toString())
                binding.checkboxHideWithSalary.isChecked = state.withSalaryOnly
                binding.btnReset.isVisible = state.isResetButtonEnabled
                binding.btnApply.isEnabled = state.isApplyButtonEnabled
            }
            is FilterScreenState.Empty -> {
                binding.btnApply.isEnabled = false
                binding.btnReset.isVisible = false
            }
        }
    }
}
