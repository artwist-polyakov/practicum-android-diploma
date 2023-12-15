package ru.practicum.android.diploma.common.utils

import java.text.DecimalFormat
import java.text.DecimalFormatSymbols

fun Int.formatSalary(): String {
    val symbols = DecimalFormatSymbols().apply {
        groupingSeparator = ' '
    }
    val formatter = DecimalFormat("#,###", symbols)
    return formatter.format(this)
}
