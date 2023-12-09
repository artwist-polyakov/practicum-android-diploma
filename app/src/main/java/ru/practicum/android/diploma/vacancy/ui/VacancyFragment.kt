package ru.practicum.android.diploma.vacancy.ui

import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.room.util.appendPlaceholders
import coil.transform.RoundedCornersTransformation
import com.google.android.material.bottomnavigation.BottomNavigationView
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch
import ru.practicum.android.diploma.R
import ru.practicum.android.diploma.common.data.network.NetworkClient
import ru.practicum.android.diploma.common.ui.BaseFragment
import ru.practicum.android.diploma.databinding.FragmentVacancyBinding
import ru.practicum.android.diploma.vacancy.domain.models.DetailedVacancyItem
import javax.inject.Inject

@AndroidEntryPoint
class VacancyFragment : BaseFragment<FragmentVacancyBinding, VacancyViewModel>(FragmentVacancyBinding::inflate) {
    override val viewModel: VacancyViewModel by viewModels()
    private lateinit var bottomNavigator: BottomNavigationView

    @Inject
    lateinit var networkClient: NetworkClient

    override fun initViews() {
        val id = arguments?.getString("id") ?: null
        id?.let {
            viewModel.getVacancy(it.toInt())
        }

    }

    override fun subscribe() {
        viewLifecycleOwner.lifecycleScope.launch {
            viewModel.state.collect { state ->
                Log.d(MYLOG, "mock data $state")
            }
        }
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        Log.i(MYLOG, "onViewCreated")
        val id = arguments?.getString("id") ?: null
        Log.d(MYLOG, "id $id")
        id?.let {
            Log.d(MYLOG, "id $it")
            viewModel.getVacancy(it.toInt())
        }

    }

    fun vacancyDrawer(item: DetailedVacancyItem) {
        //панель навигации
        bottomNavigator = requireActivity().findViewById(R.id.bottom_navigation_view)
        bottomNavigator.visibility = View.GONE

        //отрабатываем стрелку назад
        val fragmentmanager = requireActivity().supportFragmentManager
        binding.b_back_arrow.setOnClickListener(
            bottomNavigator.visibility = View.VISIBLE
            fragmentmanager.popBackStack()
        )

        //отрисовка вакансии
        binding.tv_vacancy_name.text=item.title
        binding.tv_min_salary_text.text=item.salaryFrom
        binding.tv_max_salary_text.text=item.salaryTo
        binding.employer_label.load(item.employerLogo) {
            placeholder(R.drawable.placeholder_48px)
            transformations(RoundedCornersTransformation(R.dimen.vacancy_logo_roundcorners.toFloat()))
        }
        binding.tv_employer_text.text=item.employerName
        binding.tv_city_text.text=item.area
        binding.tv_experience.text=item.experience
        binding.tv_schedule.text=item.schedule
        binding.


    }

    companion object {
        const val MYLOG = "VacancyMyLog"
    }
}
