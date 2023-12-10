package ru.practicum.android.diploma.vacancy.ui

import android.os.Bundle
import android.text.Html
import android.util.Log
import androidx.core.view.isVisible
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import coil.load
import coil.transform.RoundedCornersTransformation
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch
import ru.practicum.android.diploma.R
import ru.practicum.android.diploma.common.data.network.NetworkClient
import ru.practicum.android.diploma.common.ui.BaseFragment
import ru.practicum.android.diploma.databinding.FragmentVacancyBinding
import ru.practicum.android.diploma.vacancy.domain.models.DetailedVacancyItem
import ru.practicum.android.diploma.vacancy.domain.models.VacancyState
import javax.inject.Inject

@AndroidEntryPoint
class VacancyFragment : BaseFragment<FragmentVacancyBinding, VacancyViewModel>(FragmentVacancyBinding::inflate) {
    override val viewModel: VacancyViewModel by viewModels()
    private var id: Int? = null
    private var url: String = ""

    @Inject
    lateinit var networkClient: NetworkClient

    override fun initViews() {
        id = arguments?.getInt(ARG_ID)
        id?.let {
            viewModel.getVacancy(it)
        }
    }

    override fun subscribe() {
        binding.btnToSimilarVacations.setOnClickListener {
            val bundle = Bundle().apply {
                putInt(ARG_ID, id ?: 0)
            }
            findNavController().navigate(
                R.id.action_vacancyFragment_to_similarVacanciesFragment,
                bundle
            )
        }

        binding.ivArrowBack.setOnClickListener {
            findNavController().popBackStack()
        }

        binding.ivShareButton.setOnClickListener {
            Log.i(MYLOG, "url = $url")
            viewModel.shareVacancy(url)
        }

        binding.ivLikeButton.setOnClickListener {
            // добавить обработчик
        }

        viewLifecycleOwner.lifecycleScope.launch {
            viewModel.state.collect { state ->
                renderState(state)
            }
        }
    }

    private fun fetchScreen(item: DetailedVacancyItem) {
        url = "https://hh.ru/vacancy/ " + item.id
        // Отрисовка вакансии
        with(binding) {
            tvVacancyName.text = item.title
            tvSalary.text = when {
                item.salaryFrom == null -> getString(R.string.salary_not_specified)
                item.salaryTo == null -> getString(
                    R.string.salary_from,
                    item.salaryFrom.toString(),
                    item.salaryCurrency
                )

                else -> getString(
                    R.string.salary_from_to,
                    item.salaryFrom.toString(),
                    item.salaryTo.toString(),
                    item.salaryCurrency
                )
            }

            employerLabel.load(item.employerLogo) {
                placeholder(R.drawable.placeholder_48px)
                transformations(RoundedCornersTransformation(R.dimen.button_radius.toFloat()))
            }

            tvDescriptionText.text = Html.fromHtml(item.description, Html.FROM_HTML_MODE_COMPACT)
            tvEmployerText.text = item.employerName
            tvCityText.text = item.area
            tvExperience.text = item.experience
            tvSchedule.text = getString(R.string.schedule, item.schedule, item.employment)
            tvKeySkillsDescription.text = item.keySkills.toString()
        }
    }

    private fun renderState(state: VacancyState) = with(binding) {
        when (state) {
            is VacancyState.Content -> {
                fetchScreen(state.vacancy)
                clBody.isVisible = true
                progressBar.isVisible = false
            }

            else -> {
                clBody.isVisible = false
                progressBar.isVisible = true
            }
        }
    }

    companion object {
        const val MYLOG = "VacancyMyLog"
        const val ARG_ID = "id"
        const val CLICK_DEBOUNCE_DELAY_500MS = 500L
    }
}
