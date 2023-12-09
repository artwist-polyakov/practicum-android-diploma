package ru.practicum.android.diploma.vacancy.ui

import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.core.os.bundleOf
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import coil.load
import coil.transform.RoundedCornersTransformation
import com.google.android.material.bottomnavigation.BottomNavigationView
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
            Log.d(MYLOG, "vacancyId $id")
            findNavController().navigate(R.id.action_vacancyFragment_to_similarVacanciesFragment, bundle)
        }
        viewLifecycleOwner.lifecycleScope.launch {
            viewModel.state.collect { state ->
                if (state is VacancyState.Content) fetchScreen(state.vacancy)
                Log.d(MYLOG, "mock data $state")
            }
        }
    }

    fun fetchScreen(item: DetailedVacancyItem) {
        // отрабатываем стрелку назад
        val fragmentmanager = requireActivity().supportFragmentManager
        binding.bBackArrow.setOnClickListener {
            fragmentmanager.popBackStack()
        }

        // Отрабатываем кнопку "поделиться"
        val url = "https://hh.ru/vacancy/ " + item.id
        binding.bShareButton.setOnClickListener {
            viewModel.shareVacancy(url)
        }
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
        fun setId(id: Int): Bundle =
            bundleOf(ARG_ID to id)
    }
}
