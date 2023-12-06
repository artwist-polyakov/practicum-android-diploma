package ru.practicum.android.diploma.search.domain.models

data class SingleTreeElement(
    val id: String,
    val name: String,
    val parent: Int?,
    val hasChildrens: Boolean,
    val children: List<SingleTreeElement>? = null
)
