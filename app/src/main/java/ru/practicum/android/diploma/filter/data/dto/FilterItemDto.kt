package ru.practicum.android.diploma.filter.data.dto

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class FilterItemDto(
    val id: Int? = null,
    val name: String? = null
) : Parcelable
