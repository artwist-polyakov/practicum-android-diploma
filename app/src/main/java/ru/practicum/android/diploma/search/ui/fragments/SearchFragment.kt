package ru.practicum.android.diploma.search.ui.fragments

import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch
import ru.practicum.android.diploma.common.data.network.NetworkClient
import ru.practicum.android.diploma.common.data.network.requests.AreasRequest
import ru.practicum.android.diploma.common.data.network.requests.IndustriesRequest
import ru.practicum.android.diploma.common.data.network.requests.SingleVacancyRequest
import ru.practicum.android.diploma.common.data.network.requests.VacanciesSearchRequest
import ru.practicum.android.diploma.common.data.network.response.SingleVacancyResponse
import ru.practicum.android.diploma.common.ui.BaseFragment
import ru.practicum.android.diploma.databinding.FragmentSearchBinding
import ru.practicum.android.diploma.search.data.network.HHSearchRepository
import ru.practicum.android.diploma.search.ui.viewmodels.SearchViewModel
import javax.inject.Inject

@AndroidEntryPoint
class SearchFragment : BaseFragment<FragmentSearchBinding, SearchViewModel>(FragmentSearchBinding::inflate) {
    override val viewModel: SearchViewModel by viewModels()

    @Inject
    lateinit var repo: HHSearchRepository

    override fun initViews() {
        // Блок для инициализации ui
    }

    override fun subscribe() {
        // Блок для подписок (клики, viewModel)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // todo  удалить после отладки
        viewLifecycleOwner.lifecycleScope.launch {
            repo.getVacancies(query = "android")
                .collect {
                    it.data?.items?.forEach { vacancy ->
                        Log.d("SearchFragment", "vacancy: ${vacancy.name}")
                    }
                }
            repo.getAreas()
                .collect {
                    it.data?.areas?.forEach { area ->
                        Log.d("SearchFragment", "area: ${area.name}")
                    }
                }
            repo.getIndustries()
                .collect {
                    it.data?.industries?.forEach { industry ->
                        Log.d("SearchFragment", "industry: ${industry.name}")
                    }
                }
        }
    }
}
