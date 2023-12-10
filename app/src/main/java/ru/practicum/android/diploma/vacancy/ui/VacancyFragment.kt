package ru.practicum.android.diploma.vacancy.ui

import android.os.Bundle
import android.util.Log
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
import ru.practicum.android.diploma.search.domain.models.VacancyGeneral
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

        binding.bBackArrow.setOnClickListener {
            findNavController().popBackStack()
        }

        binding.bShareButton.setOnClickListener {
            Log.i(MYLOG, "url = $url")
            viewModel.shareVacancy(url)
        }

        binding.bLikeButton.setOnClickListener {
            // добавить обработчик
        }

        viewLifecycleOwner.lifecycleScope.launch {
            viewModel.state.collect { state ->
                if (state is VacancyState.Content) fetchScreen(state.vacancy)
                Log.d(MYLOG, "mock data $state")
            }
        }
    }

    private fun fetchScreen(item: DetailedVacancyItem) {
        url = "https://hh.ru/vacancy/ " + item.id
        // Отрисовка вакансии
        with(binding) {
            tvVacancyName.text = item.title
            tvMinSalaryText.text = item.salaryFrom.toString()
            tvMaxSalaryText.text = item.salaryTo.toString()
            employerLabel.load(item.employerLogo) {
                placeholder(R.drawable.placeholder_48px)
                transformations(RoundedCornersTransformation(R.dimen.vacancy_logo_roundcorners.toFloat()))
            }
            tvEmployerText.text = item.employerName
            tvCityText.text = item.area
            tvExperience.text = item.experience
            tvSchedule.text = item.schedule + " " + item.employment
            tvJobFunctions.text = item.description
            tvJobSkills.text = item.keySkills.toString()
        }
    }

    companion object {
        const val MYLOG = "VacancyMyLog"
        const val ARG_ID = "id"
        const val CLICK_DEBOUNCE_DELAY_10MS = 10L
    }
}
