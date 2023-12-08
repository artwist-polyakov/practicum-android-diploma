package ru.practicum.android.diploma.search.ui.fragments

import android.util.Log
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
                Log.i("SearchFragmentContentMyLog", "content ${state.data.vacancies}")
//                val bundle = Bundle().apply {
//                    putString("id", state.data.vacancies[0].id.toString())
//                }
//                findNavController().navigate(R.id.action_searchFragment_to_vacancyFragment, bundle)

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
}
