package ru.practicum.android.diploma.vacancy.domain.models

import com.google.gson.annotations.SerializedName

data class Employer(
    val id: Int,
    val name: String,
    val logoUrls: Logo?,
)
