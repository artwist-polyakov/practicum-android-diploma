package ru.practicum.android.diploma.filter.ui.fragment

import androidx.fragment.app.viewModels
import androidx.lifecycle.withResumed
import androidx.navigation.fragment.findNavController
import ru.practicum.android.diploma.common.ui.BaseFragment
import ru.practicum.android.diploma.databinding.FragmentWorkPlaceBinding
import ru.practicum.android.diploma.filter.ui.viewmodel.WorkPlaceViewModel

class WorkPlaceFragment : BaseFragment<FragmentWorkPlaceBinding, WorkPlaceViewModel>(FragmentWorkPlaceBinding::inflate) {
    override val viewModel by viewModels<WorkPlaceViewModel>()
    override fun initViews() {
    }

    override fun subscribe() : Unit = with(binding) {
        ivArrowBack.setOnClickListener {
            findNavController().popBackStack()
        }
    }
}
