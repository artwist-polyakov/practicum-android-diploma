package ru.practicum.android.diploma.filter.ui.fragment

import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch
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

    }

    @Suppress("detekt.EmptyFunctionBlock")
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
    }

    private fun render(state: IndustryScreenState) {
        when (state) {
            is IndustryScreenState.Content -> {
                industryListAdapter.setData(state.data)
            }

            is IndustryScreenState.Error -> {

            }

            is IndustryScreenState.Default -> {

            }
        }
    }
}
