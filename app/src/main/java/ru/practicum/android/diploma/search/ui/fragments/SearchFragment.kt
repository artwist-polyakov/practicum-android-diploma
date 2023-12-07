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
import ru.practicum.android.diploma.search.ui.viewmodels.states.ViewModelInteractionState

@AndroidEntryPoint
class SearchFragment : BaseFragment<FragmentSearchBinding, SearchViewModel>(FragmentSearchBinding::inflate) {
    override val viewModel: SearchViewModel by viewModels()

    override fun initViews() {
//        viewModel.handleInteraction(ViewModelInteractionState.setQuery("android"))
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

        }
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        viewModel.handleInteraction(ViewModelInteractionState.setQuery("android"))
    }
}
