package ru.practicum.android.diploma.common.data.dto

import android.os.Parcelable
import com.google.gson.annotations.SerializedName
import kotlinx.parcelize.Parcelize

@Parcelize
data class LogoDto(
    val original: String,
    @SerializedName("90") val little: String,
    @SerializedName("240") val medium: String,
) : Parcelable
