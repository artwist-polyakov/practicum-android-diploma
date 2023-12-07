package ru.practicum.android.diploma.search.ui.fragments

import android.util.Log
import android.view.View
import androidx.core.widget.doOnTextChanged
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch
import ru.practicum.android.diploma.R
import ru.practicum.android.diploma.common.ui.BaseFragment
import ru.practicum.android.diploma.databinding.FragmentSearchBinding
import ru.practicum.android.diploma.search.ui.viewmodels.SearchViewModel
import ru.practicum.android.diploma.search.ui.viewmodels.states.SearchScreenState

@AndroidEntryPoint
class SearchFragment : BaseFragment<FragmentSearchBinding, SearchViewModel>(FragmentSearchBinding::inflate) {
    override val viewModel: SearchViewModel by viewModels()

    override fun initViews() {
        binding.tiSearchField.requestFocus()
    }

    // Блок для подписок (клики, viewModel)
    override fun subscribe() {
        viewLifecycleOwner.lifecycleScope.launch {
            viewModel.state.collect { state ->
                render(state)
            }
        }

        binding.tiSearchField.doOnTextChanged { text, start, before, count ->
            viewModel.getVacancies(text.toString())
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
            is SearchScreenState.Default -> {
                binding.progressBar.visibility = View.GONE
                binding.vacancyList.root.visibility = View.GONE
                binding.llStateLayout.visibility = View.VISIBLE
                binding.tvStateText.visibility = View.GONE
                binding.ivStateImage.visibility = View.VISIBLE
                binding.ivStateImage.setImageResource(R.drawable.image_search)
                Log.i("SearchFragmentDefaultMyLog", "Default state")
            }

            is SearchScreenState.Content -> {
                binding.progressBar.visibility = View.GONE
                binding.vacancyList.root.visibility = View.VISIBLE
                binding.llStateLayout.visibility = View.GONE
                Log.i("SearchFragmentContentMyLog", "Content state: ${state.vacancies}")
            }

            is SearchScreenState.Error -> {
                binding.progressBar.visibility = View.GONE
                binding.vacancyList.root.visibility = View.GONE
                binding.llStateLayout.visibility = View.VISIBLE
                binding.tvStateText.apply {
                    visibility = View.VISIBLE
                    text = resources.getString(state.error.messageResource)
                }
                binding.ivStateImage.apply {
                    visibility = View.VISIBLE
                    binding.ivStateImage.setImageResource(state.error.imageResource)
                }
                Log.i("SearchFragmentErrorMyLog", "Error state")
            }

            is SearchScreenState.Loading -> {
                binding.progressBar.visibility = View.VISIBLE
                binding.vacancyList.root.visibility = View.GONE
                binding.llStateLayout.visibility = View.GONE
                Log.i("SearchFragmentLoadingMyLog", "Loading state")
            }
        }
    }
}
