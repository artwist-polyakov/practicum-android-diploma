package ru.practicum.android.diploma.common.data.dto

import com.google.gson.annotations.SerializedName

data class EmployerDto(
    val id: Int,
    val name: String,
    @SerializedName("logo_urls") val logoUrls: LogoDto?,
)
