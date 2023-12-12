package ru.practicum.android.diploma.vacancy.domain.models

import android.util.Log

@Suppress("Detekt.DataClassContainsFunctions")
data class DetailedVacancyItem(
    val id: Int,
    val title: String,
    val employerName: String?,
    val employerLogo: String?,
    val area: String?,
    val haveSalary: Boolean = false,
    val salaryFrom: Int? = null,
    val salaryTo: Int? = null,
    val salaryCurrency: String? = null,
    val experience: String?,
    val employment: String?,
    val schedule: String?,
    val description: String?,
    val keySkills: List<String>?,
    val contacts: Contacts?,
    val favorite: Boolean = false
) {
    fun configureHtml(): String {
        val builder: StringBuilder = StringBuilder()
        builder.append(description)
        keySkills?.let {
            builder.append("<span class=\"title\">Ключевые навыки</span>")
            builder.append("<ul>")
            for (skill in it) {
                builder.append("<li>")
                builder.append(skill)
                builder.append("</li>")
            }
            builder.append("</ul>")
        }
        configureHTMLContacts(builder)
        return builder.toString()
    }

    private fun configureHTMLContacts(builder: StringBuilder): StringBuilder {
        if (contacts?.name.isNullOrEmpty() == false) {
            contacts?.let {
                builder.append("<span class=\"title\">Контакты</span>")
                builder.append("<div class=\"margin\"><span class=\"contact-info\">Контактное лицо</span><br>")
                builder.append(it.name)
                builder.append("</div>")
                builder.append("<div class=\"margin\">")
                builder.append("<span class=\"contact-info\">E-mail</span>")
                builder.append("<br><a href=\"mailto:${it.email}\">")
                builder.append(it.email)
                builder.append("</a><br>")
                builder.append("</div>")
                configureHTMLPhones(builder, it.phones)
            }
        }
        return builder
    }


    private fun configureHTMLPhones(builder: StringBuilder, phones: List<Pair<String, String>>?): StringBuilder {
        phones?.let { phones ->
            for (phone in phones) {
                val cleanPhone = phone.second.replace(Regex("[^+\\d]"), "") // Очистка телефона
                builder.append("<div class=\"margin\">")
                builder.append("<span class=\"contact-info\">Телефон</span>")
                builder.append("<br><a href=\"tel:$cleanPhone\">")
                builder.append(cleanPhone)
                builder.append("</a><br>")
                builder.append("</div>")
                phone.first?.let { comment ->
                    builder.append("<div class=\"margin\">")
                    builder.append("<span class=\"contact-info\">Комментарий</span>")
                    builder.append("<br>")
                    builder.append(comment)
                    builder.append("<br></div>")
                }
            }
        }
        return builder
    }
}
