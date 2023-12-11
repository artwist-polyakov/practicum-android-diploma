package ru.practicum.android.diploma.vacancy.ui

import android.graphics.Color
import android.os.Bundle
import android.view.View
import androidx.core.content.ContextCompat
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import coil.load
import coil.transform.RoundedCornersTransformation
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch
import ru.practicum.android.diploma.R
import ru.practicum.android.diploma.common.data.network.NetworkClient
import ru.practicum.android.diploma.common.ui.BaseFragment
import ru.practicum.android.diploma.common.utils.CssStyle
import ru.practicum.android.diploma.common.utils.formatSalary
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
        binding.wvDescription.setBackgroundColor(Color.TRANSPARENT) // установка цвета из атрибутов не отрабатывается

        id = arguments?.getInt(ARG_ID)
        id?.let {
            viewModel.getVacancy(it)
        }
    }

    override fun subscribe() {
        with(binding) {
            btnToSimilarVacations.setOnClickListener {
                val bundle = Bundle().apply {
                    putInt(ARG_ID, id ?: 0)
                }
                findNavController().navigate(
                    R.id.action_vacancyFragment_to_similarVacanciesFragment,
                    bundle
                )
            }

            ivArrowBack.setOnClickListener {
                findNavController().popBackStack()
            }

            ivShareButton.setOnClickListener {
                viewModel.shareVacancy(url)
            }

            ivLikeButton.setOnClickListener {
                viewModel.interactWithLike(id ?: 0)
            }
        }

        viewLifecycleOwner.lifecycleScope.launch {
            viewModel.state.collect { state ->
                renderState(state)
            }
        }
    }

    private fun fetchScreen(item: DetailedVacancyItem) {
        url = "https://hh.ru/vacancy/ " + item.id
        with(binding) {
            tvVacancyName.text = item.title
            fetchSalary(item)
            fetchWebview(item)
            ivEmployerLogo.load(item.employerLogo) {
                placeholder(R.drawable.placeholder_48px)
                transformations(
                    RoundedCornersTransformation(
                        radius = resources.getDimensionPixelSize(R.dimen.button_radius).toFloat()
                    )
                )
            }

            tvEmployerText.text = item.employerName
            tvCityText.text = item.area
            tvExperience.text = item.experience
            tvSchedule.text = getString(R.string.schedule, item.schedule, item.employment)
            if (item.keySkills?.isEmpty() == true) {
                rvKeySkills.visibility = View.GONE
                tvKeySkillsTitle.visibility = View.GONE
            } else {
                rvKeySkills.layoutManager = LinearLayoutManager(requireContext())
                rvKeySkills.adapter = KeySkillsAdapter(item.keySkills ?: emptyList())
            }
            ivLikeButton.setImageResource(
                if (item.favorite) {
                    R.drawable.favorites_active_24px
                } else {
                    R.drawable.favorites_inactive_24px
                }
            )
        }
    }

    private fun fetchSalary(item: DetailedVacancyItem) = with(binding) {
        tvSalary.text = when {
            item.salaryFrom == null -> getString(R.string.salary_not_specified)
            item.salaryTo == null -> getString(
                R.string.salary_from, item.salaryFrom.formatSalary(), item.salaryCurrency
            )

            else -> getString(
                R.string.salary_from_to,
                item.salaryFrom.formatSalary(),
                item.salaryTo.formatSalary(),
                item.salaryCurrency
            )
        }
    }

    private fun fetchWebview(item: DetailedVacancyItem) {
        val colorInt = ContextCompat.getColor(requireContext(), R.color.htmlText)
        val colorHex = String.format(COLOR_LOCALE, COLOR_FORMAT and colorInt)
        val modifiedHtmlContent = getString(R.string.html_content, CssStyle.getStyle(colorHex), item.description)
        if (item.description != null) {
            binding.wvDescription.loadDataWithBaseURL(null, modifiedHtmlContent, TXT_HTML, UTF_8, null)
        }
    }

    private fun renderState(state: VacancyState) = with(binding) {
        when (state) {
            is VacancyState.Content -> {
                fetchScreen(state.vacancy)
                clBody.visibility = View.VISIBLE
                progressBar.visibility = View.GONE
                tvStateError.visibility = View.GONE
            }

            is VacancyState.Loading -> {
                clBody.visibility = View.GONE
                progressBar.visibility = View.VISIBLE
                tvStateError.visibility = View.GONE
            }

            else -> {
                clBody.visibility = View.GONE
                progressBar.visibility = View.GONE
                tvStateError.visibility = View.VISIBLE
            }
        }
    }

    companion object {
        const val MYLOG = "VacancyMyLog"
        const val ARG_ID = "id"
        const val TXT_HTML = "text/html"
        const val UTF_8 = "utf-8"
        const val COLOR_LOCALE = "#%06X"
        const val COLOR_FORMAT = 0xFFFFFF
        const val CLICK_DEBOUNCE_DELAY_500MS = 500L
    }
}
