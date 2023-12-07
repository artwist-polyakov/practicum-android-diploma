package ru.practicum.android.diploma.vacancy.domain.models

data class Salary(
    val from: Int?,
    val to: Int?,
    val currency: String,
    val gross: Boolean
)
