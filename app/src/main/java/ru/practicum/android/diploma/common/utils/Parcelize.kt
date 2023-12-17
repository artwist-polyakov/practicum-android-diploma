package ru.practicum.android.diploma.common.utils

import android.os.Build.VERSION.SDK_INT
import android.os.Bundle
import android.os.Parcelable


/**
 * Рамширение parcelable учитывает это тот же parcelable для передачи объектов только с учетом SDK
 */
inline fun <reified T : Parcelable> Bundle.parcelable(key: String): T? = when {
    SDK_INT >= TIRAMISU_VERSION -> getParcelable(key, T::class.java)
    else ->
        @Suppress("DEPRECATION")
        getParcelable(key)
            as? T
}

const val TIRAMISU_VERSION = 33
