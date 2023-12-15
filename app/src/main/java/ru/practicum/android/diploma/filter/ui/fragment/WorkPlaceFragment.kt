package ru.practicum.android.diploma.filter.ui.fragment

import androidx.core.content.ContextCompat
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
    private var defaultHintColor: Int = 0
    private var activeFilterHintColor: Int = 0

    override fun initViews() {
        defaultHintColor = ContextCompat.getColor(requireContext(), R.color.inputTextHint)
        activeFilterHintColor = ContextCompat.getColor(requireContext(), R.color.textHintApearence)
    }

    override fun subscribe(): Unit = with(binding) {
        filterFieldListeners()

        ivArrowBack.setOnClickListener {
            findNavController().popBackStack()
        }

        tiCountry.setOnClickListener { Unit }
        tiRegion.setOnClickListener { Unit }
    }

    private fun filterFieldListeners() = with(binding) {
        tiCountry.setupTextChangeListener(tlCountry, ivArrowForwardCountry, requireContext())
        tiRegion.setupTextChangeListener(tlRegion, ivArrowForwardRegion, requireContext())
    }
}
