package ru.practicum.android.diploma.filter.data.dto

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class FilterIndustryDto(
    val id: String? = null,
    val name: String? = null
) : Parcelable
