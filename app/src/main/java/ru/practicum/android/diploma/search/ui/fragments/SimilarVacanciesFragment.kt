package ru.practicum.android.diploma.search.ui.fragments

import android.util.Log
import android.view.View
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch
import ru.practicum.android.diploma.R
import ru.practicum.android.diploma.common.ui.BaseFragment
import ru.practicum.android.diploma.databinding.FragmentSimilarVacanciesBinding
import ru.practicum.android.diploma.search.domain.models.VacancyGeneral
import ru.practicum.android.diploma.search.ui.adapter.VacancyAdapter
import ru.practicum.android.diploma.search.ui.viewmodels.SimilarVacanciesViewModel
import ru.practicum.android.diploma.search.ui.viewmodels.states.ErrorsSearchScreenStates
import ru.practicum.android.diploma.search.ui.viewmodels.states.SearchScreenState
import ru.practicum.android.diploma.vacancy.ui.VacancyFragment

@AndroidEntryPoint
class SimilarVacanciesFragment :
    BaseFragment<FragmentSimilarVacanciesBinding, SimilarVacanciesViewModel>(FragmentSimilarVacanciesBinding::inflate) {
    override val viewModel: SimilarVacanciesViewModel by viewModels()
    private var onVacancyClickDebounce: ((VacancyGeneral) -> Unit)? = null
    private val vacancyListAdapter = VacancyAdapter { data ->
        onVacancyClickDebounce?.invoke(data)
    }

    override fun initViews() {
        val vacancyId = arguments?.getInt(VacancyFragment.ARG_ID)
        if (vacancyId != null) {
            viewModel.getVacancyList(vacancyId)
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
                renderState(state)
            }
        }
    }


    private fun renderState(state: SearchScreenState) {
        when (state) {
            is SearchScreenState.Content -> {
                Log.d("SimilarFragmentContentMyLog", "content ${state.vacancies}")
                showData(state.vacancies)
            }

            is SearchScreenState.Error -> {
                Log.d("SimilarFragmentErrorMyLog", "error message ${state.error}")
                showProblem(state.error)
            }

            is SearchScreenState.Loading -> {
                Log.d("SimilarFragmentLoadingMyLog", "Loading state")
                showProgressBar()
            }

            is SearchScreenState.Default -> {
                Log.d("SimilarFragmentLoadingMyLog", "Default state")
                showDefault()
            }
        }
    }

    private fun showDefault() {
        with(binding) {
            vacancyList.root.visibility = View.GONE
            progressBar.visibility = View.GONE

            llProblemLayout.visibility = View.VISIBLE
            ivStateImage.setImageResource(R.drawable.image_search)
            tvStateText.visibility = View.GONE
        }
    }

    private fun showProblem(error: ErrorsSearchScreenStates) {
        with(binding) {
            vacancyList.root.visibility = View.GONE
            progressBar.visibility = View.GONE

            llProblemLayout.visibility = View.VISIBLE
            ivStateImage.setImageResource(error.imageResource)
            tvStateText.visibility = View.VISIBLE
            tvStateText.text = getString(error.messageResource)
        }
    }

    private fun showProgressBar() {
        with(binding) {
            llProblemLayout.visibility = View.GONE
            vacancyList.root.visibility = View.GONE
            progressBar.visibility = View.VISIBLE
        }
    }

    private fun showData() {
        with(binding) {
            llProblemLayout.visibility = View.GONE
            progressBar.visibility = View.GONE
            vacancyList.root.visibility = View.VISIBLE
        }
    }

    private fun showData(vacancies: List<VacancyGeneral>) {
        with(binding) {
            llProblemLayout.visibility = View.GONE
            progressBar.visibility = View.GONE
            vacancyList.root.visibility = View.VISIBLE
        }
        vacancyListAdapter.setData(vacancies)
    }

    private fun addData(vacancies: List<VacancyGeneral>) {
        showData()
        vacancyListAdapter.addData(vacancies)
    }
}
