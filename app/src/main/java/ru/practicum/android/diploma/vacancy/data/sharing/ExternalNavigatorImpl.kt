package ru.practicum.android.diploma.vacancy.data.sharing

import android.app.Application
import android.content.Intent
import ru.practicum.android.diploma.vacancy.domain.api.ExternalNavigator

class ExternalNavigatorImpl(private val application: Application) : ExternalNavigator {
    override fun shareVacancy(url: String) {
        val intentSend = Intent(Intent.ACTION_SEND)
        intentSend.type = "text/plain"
        intentSend.putExtra(Intent.EXTRA_TEXT, url)
        intentSend.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
        application.startActivity(intentSend)
    }
}
