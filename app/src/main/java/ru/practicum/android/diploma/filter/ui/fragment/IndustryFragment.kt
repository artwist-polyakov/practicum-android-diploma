package ru.practicum.android.diploma.filter.ui.fragment

import android.view.View
import androidx.core.view.isVisible
import androidx.core.widget.doOnTextChanged
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch
import ru.practicum.android.diploma.R
import ru.practicum.android.diploma.common.ui.BaseFragment
import ru.practicum.android.diploma.databinding.FragmentIndustryBinding
import ru.practicum.android.diploma.filter.ui.viewmodel.IndustryViewModel
import ru.practicum.android.diploma.filter.ui.viewmodel.states.IndustryScreenState

@AndroidEntryPoint
class IndustryFragment : BaseFragment<FragmentIndustryBinding, IndustryViewModel>(
    FragmentIndustryBinding::inflate
) {
    override val viewModel by viewModels<IndustryViewModel>()

    private val industryListAdapter = IndustryAdapter() { data ->
        binding.btnSelect.isVisible = viewModel.setIndustry(data)
    }

    override fun initViews() {
        binding.industryList.root.apply {
            layoutManager = LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false)
            adapter = industryListAdapter
        }
    }

    override fun subscribe(): Unit = with(binding) {
        viewLifecycleOwner.lifecycleScope.launch {
            viewModel.state.collect { state ->
                render(state)
            }
        }
        ivArrowBack.setOnClickListener {
            findNavController().popBackStack()
        }

        tiSearchField.doOnTextChanged { text, _, _, _ ->
            industryListAdapter.setFilter(text.toString())
            if (text.toString().isNotEmpty()) {
                ivSearchFieldButton.setImageResource(R.drawable.close_24px)
            } else {
                ivSearchFieldButton.setImageResource(R.drawable.search_24px)
            }
        }

        ivSearchFieldButton.setOnClickListener {
            if (tiSearchField.text.toString().isNotEmpty()) {
                tiSearchField.text?.clear()
            }
        }

        btnSelect.setOnClickListener {
            viewModel.saveIndustryToPref()
        }
    }

    private fun render(state: IndustryScreenState) {
        when (state) {
            is IndustryScreenState.Content -> {
                industryListAdapter.setSelectedIndustry(state.currentIndustry)
                industryListAdapter.setData(state.data)
                binding.industryList.root.visibility = View.VISIBLE
                binding.tvErrorMessage.visibility = View.GONE
                binding.progressBar.visibility = View.GONE
                binding.btnSelect.visibility = View.GONE
            }

            is IndustryScreenState.Error -> {
                binding.industryList.root.visibility = View.GONE
                binding.tvErrorMessage.visibility = View.VISIBLE
                binding.progressBar.visibility = View.GONE
                binding.btnSelect.visibility = View.GONE
            }

            is IndustryScreenState.Default -> {
                binding.industryList.root.visibility = View.GONE
                binding.tvErrorMessage.visibility = View.GONE
                binding.progressBar.visibility = View.VISIBLE
                binding.btnSelect.visibility = View.GONE
            }

            IndustryScreenState.Hide -> findNavController().popBackStack()
        }
    }

    companion object {
        const val INDUSTRY_KEY = "industry"
    }
}
