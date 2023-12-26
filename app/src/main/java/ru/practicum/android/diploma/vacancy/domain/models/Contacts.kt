package ru.practicum.android.diploma.vacancy.domain.models

typealias phone = String
typealias comment = String

data class Contacts(
    val name: String? = null,
    val phones: List<Pair<comment, phone>>? = null,
    val email: String? = null,
)
