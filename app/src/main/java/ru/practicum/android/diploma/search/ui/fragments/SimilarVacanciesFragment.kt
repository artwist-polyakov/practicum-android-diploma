package ru.practicum.android.diploma.search.ui.fragments

import android.os.Bundle
import android.view.View
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch
import ru.practicum.android.diploma.R
import ru.practicum.android.diploma.common.ui.BaseFragment
import ru.practicum.android.diploma.common.utils.debounce
import ru.practicum.android.diploma.common.utils.showCustomSnackbar
import ru.practicum.android.diploma.databinding.FragmentSimilarVacanciesBinding
import ru.practicum.android.diploma.search.domain.models.VacancyGeneral
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
            layoutManager = LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false)
            adapter = vacancyListAdapter
            addOnScrollListener(
                object : RecyclerView.OnScrollListener() {
                    override fun onScrollStateChanged(recyclerView: RecyclerView, newState: Int) {
                        super.onScrollStateChanged(recyclerView, newState)
                        if (!recyclerView.canScrollVertically(1)) {
                            viewModel.loadMoreVacancies()
                        }
                    }
                }
            )
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
            useLastParam = false,
            actionWithDelay = false
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
            is SearchScreenState.Content -> showData(state.vacancies)
            is SearchScreenState.Error -> showError(state.error, state.showSnackBar)
            is SearchScreenState.Loading -> if (state.forPage == 0) showProgressBar()
            else -> showProgressBar()
        }
    }

    private fun showError(error: ErrorsSearchScreenStates) {
        with(binding) {
            vacancyList.root.visibility = View.GONE
            progressBar.visibility = View.GONE

            tvErrorMessage.visibility = View.VISIBLE
            tvErrorMessage.text = getString(error.messageResource)
            tvErrorMessage.setCompoundDrawablesWithIntrinsicBounds(0, error.imageResource, 0, 0)
        }
    }

    private fun showError(error: ErrorsSearchScreenStates, showSnackbar: Boolean = false) {
        vacancyListAdapter.setScrollLoadingEnabled(false)
        vacancyListAdapter.refreshLastItem()
        if (showSnackbar) {
            binding.root.showCustomSnackbar(getString(error.messageResource))
            debounce<Boolean>(
                CLICK_DEBOUNCE_DELAY_500MS,
                viewLifecycleOwner.lifecycleScope,
                useLastParam = false,
                actionWithDelay = false
            ) {
                vacancyListAdapter.setScrollLoadingEnabled(it)
            }.invoke(true)
            return
        } else {
            with(binding) {
                vacancyList.root.visibility = View.GONE
                progressBar.visibility = View.GONE

                tvErrorMessage.visibility = View.VISIBLE
                tvErrorMessage.setCompoundDrawablesWithIntrinsicBounds(0, error.imageResource, 0, 0)
                tvErrorMessage.text = if (error.messageResource != -1) getString(error.messageResource) else ""
            }
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
