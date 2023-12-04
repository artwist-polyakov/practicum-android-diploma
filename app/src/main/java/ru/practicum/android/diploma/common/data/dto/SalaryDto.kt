package ru.practicum.android.diploma.common.data.dto

data class SalaryDto(
    val from: Int?,
    val to: Int?,
    val currency: CurrencyIds,
    val gross: Boolean
)
