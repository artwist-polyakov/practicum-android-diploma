package ru.practicum.android.diploma.search.domain.models

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

@Parcelize
data class VacancyGeneral(
    val id: Int,
    val title: String,
    val employerName: String?,
    val employerLogo: String?,
    val haveSalary: Boolean = false,
    val salaryFrom: Int? = null,
    val salaryTo: Int? = null,
    val salaryCurrency: String? = null,
): Parcelable
