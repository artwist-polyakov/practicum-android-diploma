package ru.practicum.android.diploma.filter.ui.fragment

import androidx.core.widget.doOnTextChanged
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import dagger.hilt.android.AndroidEntryPoint
import ru.practicum.android.diploma.R
import ru.practicum.android.diploma.common.ui.BaseFragment
import ru.practicum.android.diploma.databinding.FragmentRegionBinding
import ru.practicum.android.diploma.filter.ui.viewmodel.RegionViewModel

@AndroidEntryPoint
class RegionFragment : BaseFragment<FragmentRegionBinding, RegionViewModel>(FragmentRegionBinding::inflate) {
    override val viewModel by viewModels<RegionViewModel>()
    override fun initViews() {
        Unit
    }

    override fun subscribe() {
        inputListener()

        with(binding) {
            ivSearchFieldButton.setOnClickListener {
                if (tiSearchField.text.toString().isNotEmpty()) tiSearchField.text?.clear()
            }

            ivArrowBack.setOnClickListener {
                findNavController().popBackStack()
            }
        }

    }

    private fun inputListener() = with(binding) {
        tiSearchField.doOnTextChanged { text, _, _, _ ->
            // добавить поиск по регионам
            if (text.toString().isNotEmpty()) {
                ivSearchFieldButton.setImageResource(R.drawable.close_24px)
            } else {
                ivSearchFieldButton.setImageResource(R.drawable.search_24px)
            }
        }
    }
}
