package ru.practicum.android.diploma.vacancy.ui

import android.os.Bundle
import android.util.Log
import android.view.View
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
        val id = arguments?.getString("id") ?: null
        id?.let {
            viewModel.getVacancy(it.toInt())
        }

    }

    override fun subscribe() {
        viewLifecycleOwner.lifecycleScope.launch {
            viewModel.state.collect { state ->
                Log.d(MYLOG, "mock data $state")
            }
        }
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        Log.i(MYLOG, "onViewCreated")
        val id = arguments?.getString("id") ?: null
        Log.d(MYLOG, "id $id")
        id?.let {
            Log.d(MYLOG, "id $it")
            viewModel.getVacancy(it.toInt())
        }
    }

    companion object {
        const val MYLOG = "VacancyMyLog"
    }
}
