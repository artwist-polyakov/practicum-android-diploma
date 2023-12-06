package ru.practicum.android.diploma.search.ui.fragments

import android.os.Bundle
import android.view.View
import androidx.fragment.app.viewModels
import dagger.hilt.android.AndroidEntryPoint
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
//        viewLifecycleOwner.lifecycleScope.launch {
//            repo.getVacancies(query = "android")
//                .collect {
//                    it.data?.items?.forEach { vacancy ->
//                        Log.d("SearchFragment", "vacancy: ${vacancy.contacts?.phones?.firstOrNull()?.number}")
//                    }
//                }
//            repo.getAreas()
//                .collect {
//                    it.data?.areas?.forEach { area ->
//                        Log.d("SearchFragment", "area: ${area.name}")
//                    }
//                }
//            repo.getIndustries()
//                .collect {
//                    it.data?.industries?.forEach { industry ->
//                        Log.d("SearchFragment", "industry: ${industry.name}")
//                    }
//                }
//            repo.getVacancy(90190128)
//                .collect {
//                    it.data?.vacancy?.let { vacancy ->
//                        Log.d("SearchFragment", "vacancy: ${vacancy.description}")
//                    }
//                }
//            repo.getSimilarVacancies(90190128)
//                .collect {
//                    it.data?.items?.forEach { vacancy ->
//                        Log.d("SearchFragment", "vacancy similar: ${vacancy.name}")
//                    }
//                }
//        }
    }
}
