package ru.practicum.android.diploma.common.domain.models

data class vacancyForFavoriteList(
    val vacancyId: Int,
    val employerName: String,
    val employerLogoUrls90: String?,
    val city: String,
    val jobName: String,
    val currency: String,
    val from: String,
    val to: String,
)
