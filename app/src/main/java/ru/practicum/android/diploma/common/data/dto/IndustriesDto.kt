package ru.practicum.android.diploma.common.data.dto

data class IndustriesDto(
    val id: String,
    val name: String,
    val industries: List<IndustriesDto>?
)
