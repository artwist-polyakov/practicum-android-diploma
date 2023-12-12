package ru.practicum.android.diploma.vacancy.domain.models

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
        builder.append("<br><br>")
        keySkills?.let {
            builder.append("<p class=\"title\">Ключевые навыки:</p>")
            builder.append("<ul>")
            for (skill in it) {
                builder.append("<li>")
                builder.append(skill)
                builder.append("</li>")
            }
            builder.append("</ul><br><br>")
        }
        configureHTMLContacts(builder)
        return builder.toString()
    }

    private fun configureHTMLContacts(builder: StringBuilder): StringBuilder {
        contacts?.let {
            builder.append("<p class=\"title\">Контакты:</p>")
            builder.append("<b>Контактное лицо:</b><br>")
            builder.append(it.name)
            builder.append("<br>")
            builder.append("<b>Email:</b> ")
            builder.append("<a href=\"mailto:${it.email}\">")
            builder.append(it.email)
            builder.append("</a><br>")
            configureHTMLPhones(builder, it.phones)
        }
        return builder
    }


    private fun configureHTMLPhones(builder: StringBuilder, phones: List<Pair<String, String>>?): StringBuilder {
        phones?.let { phones ->
            for (phone in phones) {
                val cleanPhone = phone.first.replace(Regex("[^+\\d]"), "") // Очистка телефона
                builder.append("<b>Телефон:</b> ")
                builder.append("<a href=\"tel:$cleanPhone\">")
                builder.append(cleanPhone)
                builder.append("</a><br>")
                phone.second?.let { comment ->
                    builder.append("<b>Комментарий:</b> ")
                    builder.append(comment)
                    builder.append("<br>")
                }
            }
        }
        return builder
    }
}
