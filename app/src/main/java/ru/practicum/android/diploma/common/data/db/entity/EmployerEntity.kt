package ru.practicum.android.diploma.common.data.db.entity

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "employers")
data class EmployerEntity(
    @PrimaryKey
    val id: Int,
    val employerEmail: String?,
    val phonesJSON: String?,
    val logosJSON: String?,
    val employerName: String,
    val employerLogoUri: String?
)
