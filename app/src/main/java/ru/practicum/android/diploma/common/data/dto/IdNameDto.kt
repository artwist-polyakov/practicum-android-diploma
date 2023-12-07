package ru.practicum.android.diploma.common.data.dto

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class IdNameDto(
    val id: String?,
    val name: String?
) : Parcelable
