package ru.practicum.android.diploma.search

import androidx.fragment.app.viewModels
import ru.practicum.android.diploma.databinding.FragmentSearchBinding
import ru.practicum.android.diploma.util.BaseFragment

class SearchFragment : BaseFragment<FragmentSearchBinding, SearchViewModel>(FragmentSearchBinding::inflate) {
    override val viewModel: SearchViewModel by viewModels()

    override fun initViews() {
        // Блок для инициализации ui
    }

    override fun subscribe() {
        // Блок для подписок (клики, viewModel)
    }
}
