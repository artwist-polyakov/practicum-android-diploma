package ru.practicum.android.diploma.vacancy.ui

import android.os.Bundle
import android.view.View
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch
import ru.practicum.android.diploma.common.data.network.NetworkClient
import ru.practicum.android.diploma.common.data.network.requests.AreasRequest
import ru.practicum.android.diploma.common.data.network.requests.IndustriesRequest
import ru.practicum.android.diploma.common.data.network.requests.SingleVacancyRequest
import ru.practicum.android.diploma.common.data.network.requests.VacanciesSearchRequest
import ru.practicum.android.diploma.common.data.network.response.SingleVacancyResponse
import ru.practicum.android.diploma.common.ui.BaseFragment
import ru.practicum.android.diploma.databinding.FragmentSearchBinding
import ru.practicum.android.diploma.databinding.FragmentVacancyBinding
import ru.practicum.android.diploma.search.ui.viewmodels.SearchViewModel
import javax.inject.Inject

@AndroidEntryPoint
class VacancyFragment : BaseFragment<FragmentVacancyBinding, VacancyViewModel>(FragmentVacancyBinding::inflate) {
    override val viewModel: VacancyViewModel by viewModels()

    @Inject
    lateinit var networkClient: NetworkClient

    override fun initViews() {
        // Блок для инициализации ui
    }

    override fun subscribe() {
        // Блок для подписок (клики, viewModel)
    }
}
