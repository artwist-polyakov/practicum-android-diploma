package ru.practicum.android.diploma.vacancy.ui

import android.util.Log
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch
import ru.practicum.android.diploma.common.data.network.NetworkClient
import ru.practicum.android.diploma.common.ui.BaseFragment
import ru.practicum.android.diploma.databinding.FragmentVacancyBinding
import javax.inject.Inject

@AndroidEntryPoint
class VacancyFragment : BaseFragment<FragmentVacancyBinding, VacancyViewModel>(FragmentVacancyBinding::inflate) {
    override val viewModel: VacancyViewModel by viewModels()

    @Inject
    lateinit var networkClient: NetworkClient

    override fun initViews() {
        viewModel.getVacancy(1511) // Mock data
    }

    override fun subscribe() {
        viewLifecycleOwner.lifecycleScope.launch {
            viewModel.state.collect {state ->
                Log.i("VacancyMyLog", "mock data $state")
            }
        }
    }
}
