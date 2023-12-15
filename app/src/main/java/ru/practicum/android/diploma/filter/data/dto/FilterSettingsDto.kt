package ru.practicum.android.diploma.filter.data.dto

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class FilterSettingsDto(
    val region: FilterItemDto = FilterItemDto(),
    val industry: FilterItemDto = FilterItemDto(),
    val salary: Int? = null,
    val withSalaryOnly: Boolean = false
) : Parcelable
