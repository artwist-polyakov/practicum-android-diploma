package ru.practicum.android.diploma.filter.data.dto

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class FilterSettingsDto(
    val region: FilterRegionDto = FilterRegionDto(),
    val industry: FilterIndustryDto = FilterIndustryDto(),
    val salary: Int? = null,
    val withSalaryOnly: Boolean = false
) : Parcelable
