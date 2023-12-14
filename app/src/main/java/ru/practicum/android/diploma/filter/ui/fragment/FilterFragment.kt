package ru.practicum.android.diploma.filter.ui.fragment

import android.content.res.ColorStateList
import androidx.core.content.ContextCompat
import androidx.core.view.isVisible
import androidx.core.widget.doOnTextChanged
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import dagger.hilt.android.AndroidEntryPoint
import ru.practicum.android.diploma.R
import ru.practicum.android.diploma.common.ui.BaseFragment
import ru.practicum.android.diploma.databinding.FragmentFilterBinding
import ru.practicum.android.diploma.filter.ui.viewmodel.FilterViewModel

@AndroidEntryPoint
class FilterFragment : BaseFragment<FragmentFilterBinding, FilterViewModel>(FragmentFilterBinding::inflate) {
    override val viewModel by viewModels<FilterViewModel>()
    private var defaultHintColor: Int = 0
    private var activeHintColor: Int = 0
    private var activeFilterHintColor: Int = 0
    override fun initViews(): Unit = with(binding) {
        defaultHintColor = ContextCompat.getColor(requireContext(), R.color.inputTextHint)
        activeHintColor = ContextCompat.getColor(requireContext(), R.color.blue)
        activeFilterHintColor = ContextCompat.getColor(requireContext(), R.color.textHintApearence)

        tiSalaryField.requestFocus()

        filterFieldManager()
    }

    override fun subscribe(): Unit = with(binding) {
        inputListener()
        filterFieldListeners()

        ivArrowBack.setOnClickListener {
            findNavController().popBackStack()
        }

        llSalaryChecbox.setOnClickListener {
            checkbox.isChecked = !checkbox.isChecked
            updateButtonBlockVisibility()
        }

        checkbox.setOnClickListener {
            updateButtonBlockVisibility()
        }

        ivInputButton.setOnClickListener {
            tiSalaryField.text?.clear()
        }

        tiWorkPlace.setOnClickListener {
            findNavController().navigate(R.id.action_filterFragment_to_workPlaceFragment)
        }

        tiIndustry.setOnClickListener { (Unit) }

        btnApply.setOnClickListener { (Unit) }

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
        tiWorkPlace.doOnTextChanged { text, _, _, _ ->
            val hintColor = if (text.toString().isEmpty()) defaultHintColor else activeFilterHintColor
            tlWorkPlace.hintTextColor = ColorStateList.valueOf(hintColor)
        }

        tiIndustry.doOnTextChanged { text, _, _, _ ->
            val hintColor = if (text.toString().isEmpty()) defaultHintColor else activeFilterHintColor
            tlIndustry.hintTextColor = ColorStateList.valueOf(hintColor)
        }
    }

    private fun filterFieldManager() = with(binding) {
        tiWorkPlace.isFocusable = false
        tiWorkPlace.isCursorVisible = false
        tiWorkPlace.keyListener = null

        tiIndustry.isFocusable = false
        tiIndustry.isCursorVisible = false
        tiIndustry.keyListener = null
    }

    private fun resetFilter() = with(binding) {
        tiWorkPlace.text = null
        tiIndustry.text = null
        tiSalaryField.text = null
        checkbox.isChecked = false
        llButtonBlock.isVisible = false
    }

    private fun updateButtonBlockVisibility() = with(binding) {
        llButtonBlock.isVisible = checkbox.isChecked || tiSalaryField.text.toString().isNotEmpty()
    }
}
