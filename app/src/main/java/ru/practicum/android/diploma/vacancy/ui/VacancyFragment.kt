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
            Log.d("VacancyMyLog", "id $it")
            viewModel.getVacancy(it.toInt())
        }

    }

    override fun subscribe() {
        viewLifecycleOwner.lifecycleScope.launch {
            viewModel.state.collect { state ->
                Log.i("VacancyMyLog", "mock data $state")
            }
        }
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        Log.i("VacancyMyLog", "onViewCreated")
        val id = arguments?.getString("id") ?: null
        Log.d("VacancyMyLog", "id $id")
        id?.let {
            Log.d("VacancyMyLog", "id $it")
            viewModel.getVacancy(it.toInt())
        }
    }

    companion object {
        private const val MOCK_ID = 90274177 // Моковый id
    }
}
