package ru.practicum.android.diploma.filter.domain.models

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class FilterRegionValue(
    val id: Int?,
    val text: String?
) : Parcelable
