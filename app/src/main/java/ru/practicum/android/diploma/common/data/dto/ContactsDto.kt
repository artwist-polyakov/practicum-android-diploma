package ru.practicum.android.diploma.common.data.dto

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class ContactsDto(
    val name: String? = null,
    val phones: List<PhoneDto>? = null,
    val email: String? = null,
) : Parcelable

