package ru.practicum.android.diploma.search.domain.models

data class SingleTreeElement(
    val id: Int,
    val name: String,
    val parent: Int?,
    val hasChildrens: Boolean
)
