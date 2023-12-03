package ru.practicum.android.diploma.common.data.dto

data class AreaDto(
    val id: String,
    val name: String,
    val areas: List<AreaDto>
)
