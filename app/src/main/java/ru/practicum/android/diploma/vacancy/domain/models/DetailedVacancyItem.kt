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
    @Suppress("StringLiteralDuplication")
    fun configureHtml(): String {
        val builder: StringBuilder = StringBuilder()
        builder.append(description)
        if (keySkills.isNullOrEmpty() == false) {
            keySkills?.let {
                builder.append("$OPEN_SPAN_STR\"title\">Ключевые навыки</span>")
                builder.append("<ul class=\"margin\">")
                for (skill in it) {
                    builder.append("<li>")
                    builder.append(skill)
                    builder.append("</li>")
                }
                builder.append("</ul>")
            }
            configureHTMLContacts(builder)
        }
        return builder.toString()
    }

    private fun configureHTMLContacts(builder: StringBuilder): StringBuilder {
        if (!contacts?.name.isNullOrEmpty()) {
            contacts?.let {
                builder.append("$OPEN_SPAN_STR\"title margin\">Контакты</span>")
                builder.append("$DIV_MARGIN<span class=\"contact-info\">Контактное лицо</span><br>")
                builder.append(it.name)
                builder.append(CLOSE_DIV_STR)
                builder.append(DIV_MARGIN)
                it.email?.let { emailstring ->
                    builder.append("$OPEN_SPAN_STR\"contact-info\">E-mail</span>")
                    builder.append("<br><a href=\"mailto:${emailstring}\">")
                    builder.append(emailstring)
                    builder.append("</a><br>")
                    builder.append(CLOSE_DIV_STR)
                }
                configureHTMLPhones(builder, it.phones)
            }
        }
        return builder
    }

    private fun configureHTMLPhones(builder: StringBuilder, phones: List<Pair<String, String>>?): StringBuilder {
        phones?.let { phones ->
            for (phone in phones) {
                val cleanPhone = formatPhoneNumber(phone.second)
                builder.append(DIV_MARGIN)
                builder.append("$OPEN_SPAN_STR\"contact-info\">Телефон</span>")
                builder.append("<br><a href=\"tel:$cleanPhone\">")
                builder.append(cleanPhone)
                builder.append("</a><br>")
                builder.append(CLOSE_DIV_STR)
                if (phone.first.isNotBlank()) {
                    builder.append(DIV_MARGIN)
                    builder.append("$OPEN_SPAN_STR\"contact-info\">Комментарий</span>")
                    builder.append("<br>")
                    builder.append(phone.first)
                    builder.append("<br>$CLOSE_DIV_STR")
                }
            }
        }
        return builder
    }

    private fun formatPhoneNumber(phone: String): String {
        val digits = phone.replace(Regex("[^\\d]"), "")
        return digits.replace(Regex("(\\d)(\\d{3})(\\d{3})(\\d{2})(\\d{2})"), "+$1 ($2) $3-$4-$5")
    }

    companion object {
        const val CLOSE_DIV_STR = "</div>"
        const val DIV_MARGIN = "<div class=\"margin\">"
        const val OPEN_SPAN_STR = "<span class="
    }
}
