package ru.practicum.android.diploma.filter.ui.fragment

import androidx.fragment.app.viewModels
import ru.practicum.android.diploma.common.ui.BaseFragment
import ru.practicum.android.diploma.databinding.FragmentRegionBinding
import ru.practicum.android.diploma.filter.ui.viewmodel.RegionViewModel

class RegionFragment : BaseFragment<FragmentRegionBinding, RegionViewModel>(FragmentRegionBinding::inflate) {
    override val viewModel by viewModels<RegionViewModel>()
    override fun initViews() {
        Unit
    }

    override fun subscribe() {
        Unit
    }
}
