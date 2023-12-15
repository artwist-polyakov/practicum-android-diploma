package ru.practicum.android.diploma.filter.ui.fragment

import android.content.res.ColorStateList
import androidx.core.content.ContextCompat
import androidx.core.widget.doOnTextChanged
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import ru.practicum.android.diploma.R
import ru.practicum.android.diploma.common.ui.BaseFragment
import ru.practicum.android.diploma.databinding.FragmentWorkPlaceBinding
import ru.practicum.android.diploma.filter.ui.viewmodel.WorkPlaceViewModel

class WorkPlaceFragment : BaseFragment<FragmentWorkPlaceBinding, WorkPlaceViewModel>(
    FragmentWorkPlaceBinding::inflate
) {
    override val viewModel by viewModels<WorkPlaceViewModel>()
    private var defaultHintColor: Int = 0
    private var activeFilterHintColor: Int = 0

    @Suppress("detekt.EmptyFunctionBlock")
    override fun initViews() {
        defaultHintColor = ContextCompat.getColor(requireContext(), R.color.inputTextHint)
        activeFilterHintColor = ContextCompat.getColor(requireContext(), R.color.textHintApearence)
    }

    override fun subscribe(): Unit = with(binding) {
        ivArrowBack.setOnClickListener {
            findNavController().popBackStack()
        }
    }

    private fun filterFieldListeners() = with(binding) {
        tiCountry.doOnTextChanged { text, _, _, _ ->
            var hintColor = 0
            if (text.toString().isEmpty()) {
                hintColor = defaultHintColor
                ivArrowForwardCountry.setImageResource(R.drawable.arrow_forward_24px)
                ivArrowForwardCountry.isClickable = false
            } else {
                hintColor = activeFilterHintColor
                ivArrowForwardCountry.setImageResource(R.drawable.ic_cross_24px)
                ivArrowForwardCountry.isClickable = true
            }
            tlCountry.hintTextColor = ColorStateList.valueOf(hintColor)
        }

        tiRegion.doOnTextChanged { text, _, _, _ ->
            var hintColor = 0
            if (text.toString().isEmpty()) {
                hintColor = defaultHintColor
                ivArrowForwardRegion.setImageResource(R.drawable.arrow_forward_24px)
                ivArrowForwardRegion.isClickable = false
            } else {
                hintColor = activeFilterHintColor
                ivArrowForwardRegion.setImageResource(R.drawable.ic_cross_24px)
                ivArrowForwardRegion.isClickable = true
            }
            tlRegion.hintTextColor = ColorStateList.valueOf(hintColor)
        }
    }
}
