package ru.practicum.android.diploma.search.ui.fragments

import androidx.fragment.app.viewModels
import dagger.hilt.android.AndroidEntryPoint
import ru.practicum.android.diploma.common.ui.BaseFragment
import ru.practicum.android.diploma.databinding.FragmentSearchBinding
import ru.practicum.android.diploma.search.ui.viewmodels.SearchViewModel

@AndroidEntryPoint
class SearchFragment : BaseFragment<FragmentSearchBinding, SearchViewModel>(FragmentSearchBinding::inflate) {
    override val viewModel by viewModels<SearchViewModel>()

    override fun initViews() {
        // Блок для инициализации ui
    }

    override fun subscribe() {
        // Блок для подписок (клики, viewModel)
    }
}
