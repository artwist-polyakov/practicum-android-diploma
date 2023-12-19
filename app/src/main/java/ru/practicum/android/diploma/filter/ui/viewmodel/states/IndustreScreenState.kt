package ru.practicum.android.diploma.filter.ui.viewmodel.states

import ru.practicum.android.diploma.search.domain.models.Industry

sealed class IndustryScreenState {
    data object Default : IndustryScreenState()
    data class Content(
        val data: List<Industry>,
        val currentIndustry: String? = null
    ): IndustryScreenState()

    data object Error : IndustryScreenState()
}
