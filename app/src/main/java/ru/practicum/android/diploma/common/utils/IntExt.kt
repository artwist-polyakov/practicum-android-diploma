package ru.practicum.android.diploma.common.utils

import android.content.Context
import android.content.res.Configuration
import android.content.res.Resources
import ru.practicum.android.diploma.R
import java.util.Locale

fun Int.getquantityString(context: Context): String {
    val configuration = Configuration(context.resources.configuration)
    configuration.setLocale(Locale("ru"))
    val localizedResources: Resources = context.createConfigurationContext(configuration).resources
    return localizedResources.getQuantityString(
        R.plurals.founded_vacancies,
        this,
        this
    )
}
