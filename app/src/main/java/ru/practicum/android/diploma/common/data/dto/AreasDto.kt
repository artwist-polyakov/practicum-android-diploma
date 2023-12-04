package ru.practicum.android.diploma.common.data.dto

data class AreasDto(
    val id: String,
    val name: String,
    val areas: List<AreasDto>?
)
