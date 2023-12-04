package ru.practicum.android.diploma.common.data.dto

import com.google.gson.annotations.SerializedName

data class LogoDto(
    val original: String,
    @SerializedName("90") val little: String,
    @SerializedName("240") val medium: String,
)
