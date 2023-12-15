package ru.practicum.android.diploma.filter.ui.fragment

import androidx.fragment.app.viewModels
import ru.practicum.android.diploma.common.ui.BaseFragment
import ru.practicum.android.diploma.databinding.FragmentLocationBinding
import ru.practicum.android.diploma.filter.ui.viewmodel.LocationViewModel

class LocationFragment : BaseFragment<FragmentLocationBinding, LocationViewModel>(FragmentLocationBinding::inflate) {
    override val viewModel by viewModels<LocationViewModel>()
    override fun initViews() {
        TODO("Not yet implemented")
    }

    override fun subscribe() {
        TODO("Not yet implemented")
    }


}
