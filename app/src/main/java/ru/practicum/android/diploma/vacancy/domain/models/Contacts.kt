package ru.practicum.android.diploma.vacancy.domain.models

// PAIR — phone - first, comment - second
data class Contacts(
    val name: String? = null,
    val phones: List<Pair<String, String>>? = null,
    val email: String? = null,
)
