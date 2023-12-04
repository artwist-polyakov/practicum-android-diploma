package ru.practicum.android.diploma.common.data.db.entity

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "employer_vacancy_table")
data class EmployerEntity(
    @PrimaryKey
    val id: Int,
    val employerEmail: String?,
    val employerContactName: String?,
    val employerPhone: String?,
    val comment: String?,
    val employerLogoUrls90: String?,
    val employerLogoUrls240: String?,
    val employerLogoUrlsOriginal: String?,
    val employerName: String,
    val employerPicture: String
)
