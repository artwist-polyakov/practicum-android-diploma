package ru.practicum.android.diploma.filter.ui.viewmodel.states

sealed class IndustryViewModelInteraction {
    data class interactWithElement(val pos: Int): IndustryViewModelInteraction()
    object saveSetup: IndustryViewModelInteraction()
    object backButtonPressed: IndustryViewModelInteraction()
}
