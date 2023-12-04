package ru.practicum.android.diploma.filter.ui.fragment

import androidx.fragment.app.viewModels
import dagger.hilt.android.AndroidEntryPoint
import ru.practicum.android.diploma.common.ui.BaseFragment
import ru.practicum.android.diploma.databinding.FragmentFilterBinding
import ru.practicum.android.diploma.filter.ui.viewmodel.FilterViewModel

@AndroidEntryPoint
class FilterFragment : BaseFragment<FragmentFilterBinding, FilterViewModel>(FragmentFilterBinding::inflate) {
    override val viewModel by viewModels<FilterViewModel>()

    override fun initViews() {
        // Блок для инициализации ui
    }

    override fun subscribe() {
        // Блок для подписок на события
    }
}
