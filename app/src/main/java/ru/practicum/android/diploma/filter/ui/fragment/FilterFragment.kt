package ru.practicum.android.diploma.filter.ui.fragment

import android.content.res.ColorStateList
import android.text.Editable
import android.text.TextWatcher
import android.view.View.GONE
import android.view.View.VISIBLE
import androidx.core.content.ContextCompat
import androidx.core.view.isVisible
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
import java.text.NumberFormat
import java.util.Locale

@AndroidEntryPoint
class FilterFragment : BaseFragment<FragmentFilterBinding, FilterViewModel>(FragmentFilterBinding::inflate) {
    override val viewModel by viewModels<FilterViewModel>()
    private var defaultHintColor: Int = 0
    private var defaultHintColorFiledInput: Int = 0
    private var activeHintColor: Int = 0
    private var filterRegionValue: FilterRegionValue? = null

    var salaryDebounce: (Int?) -> Unit = {}

    override fun initViews(): Unit = with(binding) {
        tiWorkPlace.setText(filterRegionValue?.text ?: "")

        defaultHintColor = ContextCompat.getColor(requireContext(), R.color.inputTextHint)
        defaultHintColorFiledInput = ContextCompat.getColor(requireContext(), R.color.inputTextHint)
        activeHintColor = ContextCompat.getColor(requireContext(), R.color.hint_color_selector)
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
        val salaryTextWatcher = object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) = Unit

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                updateButtonVisibility(s.toString())
                updateHintColor(s.toString())
                val cleanString = s.toString().replace(" ", "").replace("\u202F", "")
                val cleanNumberString = cleanString.replace(Regex("[^0-9]"), "")
                if (cleanNumberString.isNotBlank()) {
                    try {
                        formatSalaryText(cleanNumberString, this)
                    } catch (e: NumberFormatException) {
                        handleNumberFormattingException()
                    }
                }
                debounceSalaryInput(cleanNumberString)
            }

            override fun afterTextChanged(s: Editable?) = Unit
        }
        tiSalaryField.addTextChangedListener(salaryTextWatcher)
    }

    private fun updateButtonVisibility(text: String) {
        binding.ivInputButton.isVisible = text.isNotEmpty()
    }

    private fun updateHintColor(text: String) {
        val hintColor = if (text.isEmpty()) defaultHintColor else activeHintColor
        binding.tlSalary.hintTextColor = ColorStateList.valueOf(hintColor)
    }

    private fun formatSalaryText(cleanNumberString: String, textWatcher: TextWatcher) {
        val number = cleanNumberString.toInt()
        val formattedNumber = NumberFormat.getNumberInstance(Locale.forLanguageTag("ru-RU")).format(number)
        binding.tiSalaryField.apply {
            removeTextChangedListener(textWatcher)
            setText(formattedNumber)
            setSelection(text?.length ?: 0)
            addTextChangedListener(textWatcher)
        }
    }

    private fun handleNumberFormattingException() {
        binding.tiSalaryField.setText("")
    }

    private fun debounceSalaryInput(cleanNumberString: String) {
        salaryDebounce(if (cleanNumberString.isNotEmpty()) cleanNumberString.toIntOrNull() else null)
    }

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
