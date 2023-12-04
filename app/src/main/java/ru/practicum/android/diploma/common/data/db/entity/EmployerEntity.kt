package ru.practicum.android.diploma.common.data.db.entity

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "employere_vacancy_table")
data class EmployerEntity (
    @PrimaryKey
    val id: Int, // см. в API раздел "employer", поле "id" https://api.hh.ru/openapi/redoc#tag/Vakansii/operation/get-vacancy
    val employerEmail: String?,
    val employerContactName: String?,
    val employerPhone: List<String>?,
    val comment:String?, //комментарий из раздела Контакты
    val employerLogo_urls_90: String?,
    val employerLogo_urls_240: String?,
    val employerLogo_urls_original: String?,
    val employerName: String,
)
