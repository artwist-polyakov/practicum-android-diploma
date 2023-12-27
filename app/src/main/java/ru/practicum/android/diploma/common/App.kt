package ru.practicum.android.diploma.common

import android.app.Application
import dagger.hilt.android.HiltAndroidApp
import java.util.Locale

@HiltAndroidApp
class App : Application() {

    override fun onCreate() {
        super.onCreate()
        val locale = Locale("ru")
        Locale.setDefault(locale)
        baseContext.resources.configuration.setLocale(locale)
    }

}
