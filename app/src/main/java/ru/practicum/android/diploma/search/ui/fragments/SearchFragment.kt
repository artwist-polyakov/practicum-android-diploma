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

    companion object {
        private const val CLICK_DEBOUNCE_DELAY = 10L
    }

    override val viewModel: SearchViewModel by viewModels()
    private var onVacancyClickDebounce: ((VacancyGeneral) -> Unit)? = null
    private val vacancyListAdapter = VacancyAdapter(
        object : VacancyAdapter.VacancyClickListener {
            override fun onClick(data: VacancyGeneral) {
                onVacancyClickDebounce?.let {
                    onVacancyClickDebounce!!(data)
                }
            }
        }
    )

    override fun initViews() {
        onVacancyClickDebounce = debounce<VacancyGeneral>(
            CLICK_DEBOUNCE_DELAY,
            viewLifecycleOwner.lifecycleScope,
            false
        ) { data ->
            findNavController().navigate(
                R.id.action_searchFragment_to_vacancyFragment,
                VacancyFragment.setId(data.id)
            )
        }
        binding.vacancyList.root.apply {
            layoutManager = LinearLayoutManager(context)
            layoutManager = LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false)
            adapter = vacancyListAdapter
        }
    }

    override fun subscribe() {
        viewLifecycleOwner.lifecycleScope.launch {
            viewModel.state.collect { state ->
                render(state)
            }
        }

        binding.tiSearchField.doOnTextChanged { text, _, _, _ ->
            viewModel.handleInteraction(ViewModelInteractionState.setQuery(text.toString()))
            if (text.toString().isNotEmpty()) {
                binding.ivSearchFieldButton.setImageResource(R.drawable.close_24px)
            } else {
                binding.ivSearchFieldButton.setImageResource(R.drawable.search_24px)
            }
        }

        binding.ivSearchFieldButton.setOnClickListener {
            if (binding.tiSearchField.text.toString().isNotEmpty()) {
                binding.tiSearchField.text?.clear()
            }
        }
    }

    private fun render(state: SearchScreenState) {
        when (state) {
            is SearchScreenState.Content -> {
                Log.d("SearchFragmentContentMyLog", "content ${state.vacancies}")
                showData(state.vacancies)
            }

            is SearchScreenState.Error -> {
                Log.d("SearchFragmentErrorMyLog", "error message ${state.error}")
                showProblem(state.error)
            }

            is SearchScreenState.Loading -> {
                Log.d("SearchFragmentLoadingMyLog", "Loading state")
                showProgressBar()
            }

            is SearchScreenState.Default -> {
                Log.d("SearchFragmentLoadingMyLog", "Default state")
                showDefault()
            }
        }
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
    }

    private fun showDefault() {
        binding.vacancyList.root.visibility = View.GONE
        binding.progressBar.visibility = View.GONE

        binding.llProblemLayout.visibility = View.VISIBLE
        binding.ivStateImage.setImageResource(R.drawable.image_search)
        binding.tvStateText.visibility = View.GONE
    }

    private fun showProblem(error: ErrorsSearchScreenStates) {
        binding.vacancyList.root.visibility = View.GONE
        binding.progressBar.visibility = View.GONE

        binding.llProblemLayout.visibility = View.VISIBLE
        binding.ivStateImage.setImageResource(error.imageResource)
        binding.tvStateText.visibility = View.VISIBLE
        binding.tvStateText.text = getString(error.messageResource)
    }

    private fun showProgressBar() {
        binding.llProblemLayout.visibility = View.GONE
        binding.vacancyList.root.visibility = View.GONE

        binding.progressBar.visibility = View.VISIBLE
    }

    private fun showData() {
        binding.llProblemLayout.visibility = View.GONE
        binding.progressBar.visibility = View.GONE

        binding.vacancyList.root.visibility = View.VISIBLE
    }

    private fun showData(vacancies: List<VacancyGeneral>) {
        showData()
        vacancyListAdapter.setData(vacancies)
    }

    private fun addData(vacancies: List<VacancyGeneral>) {
        showData()
        vacancyListAdapter.addData(vacancies)
    }
}
