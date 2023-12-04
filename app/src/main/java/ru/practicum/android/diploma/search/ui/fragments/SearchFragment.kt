package ru.practicum.android.diploma.search.ui.fragments

import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch
import ru.practicum.android.diploma.common.data.network.requests.VacanciesSearchRequest
import ru.practicum.android.diploma.common.data.network.NetworkClient
import ru.practicum.android.diploma.common.data.network.requests.AreasRequest
import ru.practicum.android.diploma.common.data.network.requests.IndustriesRequest
import ru.practicum.android.diploma.common.data.network.requests.SingleVacancyRequest
import ru.practicum.android.diploma.common.data.network.response.AreaSearchResponse
import ru.practicum.android.diploma.common.data.network.response.HHSearchResponse
import ru.practicum.android.diploma.common.data.network.response.SingleVacancyResponse
import ru.practicum.android.diploma.common.ui.BaseFragment
import ru.practicum.android.diploma.databinding.FragmentSearchBinding
import ru.practicum.android.diploma.search.ui.viewmodels.SearchViewModel
import javax.inject.Inject

@AndroidEntryPoint
class SearchFragment : BaseFragment<FragmentSearchBinding, SearchViewModel>(FragmentSearchBinding::inflate) {
    override val viewModel: SearchViewModel by viewModels()

    @Inject
    lateinit var networkClient: NetworkClient

    override fun initViews() {
        // Блок для инициализации ui
    }

    override fun subscribe() {
        // Блок для подписок (клики, viewModel)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        //todo  удалить после отладки
        // начало удаляемого блока =>
        viewLifecycleOwner.lifecycleScope.launch {
            val result = networkClient.doRequest(
                VacanciesSearchRequest(
                    text = "android",
                    onlyWithSalary = true
                )
            )

            val regions = networkClient.doRequest(AreasRequest())
            val vacancy =
                (networkClient
                    .doRequest(
                        SingleVacancyRequest(
                            vacancyId = 89815858
                        )) as SingleVacancyResponse)
                    .vacancy
                    .keySkills
            val industries = networkClient.doRequest(IndustriesRequest())
            Log.d("NetworkClient", regions.resultCode.toString())
            Log.d("NetworkClient", (result as HHSearchResponse).toString())
            Log.d("NetworkClient", (regions as AreaSearchResponse).areas.toString())

            Log.d("NetworkClient", vacancy.toString())

            Log.d("NetworkClient", industries.toString())
        }
        // <= Конец удаляемого блока
    }
}
