package ru.practicum.android.diploma.search.ui.fragments

import androidx.fragment.app.viewModels
import dagger.hilt.android.AndroidEntryPoint
import ru.practicum.android.diploma.common.ui.BaseFragment
import ru.practicum.android.diploma.databinding.FragmentSimilarVacanciesBinding
import ru.practicum.android.diploma.search.ui.viewmodels.SimilarVacanciesViewModel

@AndroidEntryPoint
class SimilarVacanciesFragment :
    BaseFragment<FragmentSimilarVacanciesBinding, SimilarVacanciesViewModel>(FragmentSimilarVacanciesBinding::inflate) {
    override val viewModel: SimilarVacanciesViewModel by viewModels()
    override fun initViews() {
    }

    override fun subscribe() {
    }
}
