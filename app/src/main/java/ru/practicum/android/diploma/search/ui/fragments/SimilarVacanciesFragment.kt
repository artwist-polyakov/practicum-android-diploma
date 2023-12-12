package ru.practicum.android.diploma.search.ui.fragments

import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch
import ru.practicum.android.diploma.R
import ru.practicum.android.diploma.common.ui.BaseFragment
import ru.practicum.android.diploma.common.utils.debounce
import ru.practicum.android.diploma.databinding.FragmentSimilarVacanciesBinding
import ru.practicum.android.diploma.search.domain.models.VacancyGeneral
import ru.practicum.android.diploma.search.ui.viewmodels.SimilarVacanciesViewModel
import ru.practicum.android.diploma.search.ui.viewmodels.states.ErrorsSearchScreenStates
import ru.practicum.android.diploma.search.ui.viewmodels.states.SearchScreenState
import ru.practicum.android.diploma.search.ui.viewmodels.states.ViewModelInteractionState
import ru.practicum.android.diploma.vacancy.ui.VacancyFragment

@AndroidEntryPoint
class SimilarVacanciesFragment :
    BaseFragment<FragmentSimilarVacanciesBinding, SimilarVacanciesViewModel>(FragmentSimilarVacanciesBinding::inflate) {
    override val viewModel: SimilarVacanciesViewModel by viewModels()
    private var onVacancyClickDebounce: ((VacancyGeneral) -> Unit)? = null
    private val vacancyListAdapter = VacancyAdapter() { data ->
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
        onVacancyClickDebounce = debounce(
            CLICK_DEBOUNCE_DELAY_500MS,
            viewLifecycleOwner.lifecycleScope,
            false
        ) { data ->
            val bundle = Bundle().apply {
                putInt(VacancyFragment.ARG_ID, data.id)
            }
            findNavController().navigate(
                R.id.action_similarVacanciesFragment_to_vacancyFragment,
                bundle
            )
        }

        binding.ivArrowBack.setOnClickListener {
            findNavController().popBackStack()
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
                showEror(state.error)
            }

            else -> showProgressBar()
        }
    }

    private fun showEror(error: ErrorsSearchScreenStates) {
        with(binding) {
            vacancyList.root.visibility = View.GONE
            progressBar.visibility = View.GONE

            tvErrorMessage.visibility = View.VISIBLE
            tvErrorMessage.text = getString(error.messageResource)
            tvErrorMessage.setCompoundDrawablesWithIntrinsicBounds(0, error.imageResource, 0, 0)
        }
    }

    private fun showProgressBar() {
        with(binding) {
            tvErrorMessage.visibility = View.GONE
            progressBar.visibility = View.VISIBLE
            vacancyList.root.visibility = View.GONE
        }
    }

    private fun showData() {
        with(binding) {
            tvErrorMessage.visibility = View.GONE
            progressBar.visibility = View.GONE
            vacancyList.root.visibility = View.VISIBLE
        }
    }

    private fun showData(vacancies: List<VacancyGeneral>) {
        showData()
        vacancyListAdapter.setData(vacancies)
    }

    companion object {
        private const val CLICK_DEBOUNCE_DELAY_500MS = 500L
    }
}
