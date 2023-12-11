package ru.practicum.android.diploma.search.ui.fragments

import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.core.widget.doOnTextChanged
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch
import ru.practicum.android.diploma.R
import ru.practicum.android.diploma.common.ui.BaseFragment
import ru.practicum.android.diploma.common.utils.debounce
import ru.practicum.android.diploma.databinding.FragmentSearchBinding
import ru.practicum.android.diploma.search.domain.models.VacancyGeneral
import ru.practicum.android.diploma.search.ui.viewmodels.SearchViewModel
import ru.practicum.android.diploma.search.ui.viewmodels.states.ErrorsSearchScreenStates
import ru.practicum.android.diploma.search.ui.viewmodels.states.SearchScreenState
import ru.practicum.android.diploma.search.ui.viewmodels.states.ViewModelInteractionState
import ru.practicum.android.diploma.vacancy.ui.VacancyFragment

@AndroidEntryPoint
class SearchFragment : BaseFragment<FragmentSearchBinding, SearchViewModel>(FragmentSearchBinding::inflate) {
    override val viewModel: SearchViewModel by viewModels()
    private var onVacancyClickDebounce: ((VacancyGeneral) -> Unit)? = null
    private val vacancyListAdapter = VacancyAdapter(
        object : VacancyAdapter.ListScrollListener {
            override fun onScrollToBottom(nextPage: Int) {
                viewModel.handleInteraction(ViewModelInteractionState.setPage(nextPage))
            }
        },
    ) { data ->
        onVacancyClickDebounce?.invoke(data)
    }

    override fun initViews() {
        binding.vacancyList.root.apply {
            layoutManager = LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false)
            adapter = vacancyListAdapter
        }
    }

    override fun subscribe() {
        onVacancyClickDebounce = debounce(
            CLICK_DEBOUNCE_DELAY,
            viewLifecycleOwner.lifecycleScope,
            false,
            false
        ) { data ->
            val bundle = Bundle().apply {
                putInt(VacancyFragment.ARG_ID, data.id)
            }
            findNavController().navigate(
                R.id.action_searchFragment_to_vacancyFragment,
                bundle
            )
        }

        viewLifecycleOwner.lifecycleScope.launch {
            viewModel.state.collect { state ->
                render(state)
            }
        }

        with(binding) {
            tiSearchField.doOnTextChanged { text, _, _, _ ->
                viewModel.handleInteraction(ViewModelInteractionState.setQuery(text.toString()))
                if (text.toString().isNotEmpty()) {
                    ivSearchFieldButton.setImageResource(R.drawable.close_24px)
                } else {
                    ivSearchFieldButton.setImageResource(R.drawable.search_24px)
                }
                vacancyListAdapter.clearPageCounter()
            }
            // todo просчитать другие сценарии зануления числа страниц в адаптере
            ivSearchFieldButton.setOnClickListener {
                if (tiSearchField.text.toString().isNotEmpty()) tiSearchField.text?.clear()
            }

            ivFilter.setOnClickListener {
                findNavController().navigate(R.id.action_searchFragment_to_filterFragment)
            }
        }
    }

    private fun render(state: SearchScreenState) {
        when (state) {
            is SearchScreenState.Content -> {
                Log.d("SearchFragmentContentMyLog", "content ${state.vacancies}")
                showData(state)
            }

            is SearchScreenState.Error -> {
                Log.d("SearchFragmentErrorMyLog", "error message ${state.error}")
                showProblem(state.error)
            }

            is SearchScreenState.Loading -> {
                Log.d("SearchFragmentLoadingMyLog", "Loading state")
                if (state.forPage == 0) {
                    showCentralProgressBar()
                } else {
//                    showBottomProgressBar()
                }
            }

            is SearchScreenState.Default -> {
                Log.d("SearchFragmentLoadingMyLog", "Default state")
                showDefault()
            }
        }
    }

    private fun showDefault() {
        with(binding) {
            vacancyList.root.visibility = View.GONE
            vacancyCount.visibility = View.GONE
            progressBar.visibility = View.GONE

            llProblemLayout.visibility = View.VISIBLE
            ivStateImage.setImageResource(R.drawable.image_search)
            tvStateText.visibility = View.GONE
        }
    }

    private fun showProblem(error: ErrorsSearchScreenStates) {
        with(binding) {
            vacancyList.root.visibility = View.GONE
            vacancyCount.visibility = View.GONE
            progressBar.visibility = View.GONE

            llProblemLayout.visibility = View.VISIBLE
            ivStateImage.setImageResource(error.imageResource)
            tvStateText.visibility = View.VISIBLE
            tvStateText.text = getString(error.messageResource)
        }
    }

    private fun showCentralProgressBar() {
        with(binding) {
            llProblemLayout.visibility = View.GONE
            vacancyList.root.visibility = View.GONE
            vacancyCount.visibility = View.GONE

            progressBar.visibility = View.VISIBLE
        }
    }

//    private fun showBottomProgressBar() {
//        with(binding) {
//            llProblemLayout.visibility = View.GONE
//            vacancyList.root.visibility = View.VISIBLE
//            vacancyCount.visibility = View.VISIBLE
//
//            pbBottomProgressBar.visibility = View.VISIBLE
//        }
//    }

    private fun showData() {
        with(binding) {
            llProblemLayout.visibility = View.GONE
            progressBar.visibility = View.GONE
            pbBottomProgressBar.visibility = View.GONE
            vacancyCount.visibility = View.VISIBLE
            with(vacancyList.root) {
                visibility = View.VISIBLE
                setPadding(0, binding.vacancyCount.measuredHeight, 0, 0)
                clipToPadding = false
            }

        }
    }

    private fun showData(content: SearchScreenState.Content) {
        vacancyListAdapter.setData(content.vacancies, content.currentPage)
        binding.vacancyCount.apply {
            text = resources.getQuantityString(
                R.plurals.founded_vacancies,
                content.totalVacancies,
                content.totalVacancies
            )
            measure(0, 0)
        }
        showData()
    }

//    private fun addData(vacancies: List<VacancyGeneral>) {
//        showData()
//        vacancyListAdapter.addData(vacancies)
//    }

    companion object {
        private const val CLICK_DEBOUNCE_DELAY = 500L
    }
}
