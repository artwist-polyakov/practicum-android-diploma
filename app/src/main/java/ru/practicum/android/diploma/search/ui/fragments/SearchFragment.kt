package ru.practicum.android.diploma.search.ui.fragments

import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch
import ru.practicum.android.diploma.common.ui.BaseFragment
import ru.practicum.android.diploma.databinding.FragmentSearchBinding
import ru.practicum.android.diploma.search.ui.viewmodels.SearchViewModel
import ru.practicum.android.diploma.search.ui.viewmodels.states.SearchScreenState

@AndroidEntryPoint
class SearchFragment : BaseFragment<FragmentSearchBinding, SearchViewModel>(FragmentSearchBinding::inflate) {
    override val viewModel: SearchViewModel by viewModels()

//    @Inject
//    lateinit var interactor: SearchInteractor

    override fun initViews() {
        viewModel.getVacancies("android")
    }

    override fun subscribe() {
        viewLifecycleOwner.lifecycleScope.launch {
            viewModel.state.collect { state ->
                render(state)
            }
        }
    }

    private fun render(state: SearchScreenState) {
        when (state) {
            is SearchScreenState.Content -> {
                Log.i("SearchFragmentContentMyLog", "content ${state.vacancies}")
            }

            is SearchScreenState.Error -> {
                Log.i("SearchFragmentErrorMyLog", "error message ${state.error}")
            }

            is SearchScreenState.Loading -> {
                Log.i("SearchFragmentLoadingMyLog", "Loading state")
            }

            is SearchScreenState.Empty -> {
                Log.i("SearchFragmentEmptyMyLog", "Empty state")
            }
        }
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // todo  удалить после отладки
//        viewLifecycleOwner.lifecycleScope.launch {
//            interactor.searchVacancies(text = "android")
//                .collect {
//                    it.data?.vacancies?.forEach { vacancy ->
//                        Log.i("SearchFragmentMyLog", "vacancy: ${vacancy.title}")
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
//    }
    }
}
