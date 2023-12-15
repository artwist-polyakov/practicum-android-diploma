package ru.practicum.android.diploma.filter.ui.fragment

import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import ru.practicum.android.diploma.common.ui.BaseFragment
import ru.practicum.android.diploma.databinding.FragmentCountryBinding
import ru.practicum.android.diploma.filter.ui.viewmodel.RegionViewModel

class CountryFragment : BaseFragment<FragmentCountryBinding, RegionViewModel>(FragmentCountryBinding::inflate) {
    override val viewModel by viewModels<RegionViewModel>()
    override fun initViews() {
        Unit
    }

    override fun subscribe() {
        with(binding) {
            ivArrowBack.setOnClickListener {
                findNavController().popBackStack()
            }
        }
    }
}
