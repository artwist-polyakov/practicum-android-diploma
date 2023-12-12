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
        builder.append("<br><span class=\"title\">Описание вакансии</span>")
        builder.append(description)
        if (keySkills.isNullOrEmpty() == false) {
            keySkills?.let {
                builder.append("<span class=\"title\">Ключевые навыки</span>")
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
        if (contacts?.name.isNullOrEmpty() == false) {
            contacts?.let {
                builder.append("<span class=\"title margin\">Контакты</span>")
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
//                val cleanPhone = phone.second.replace(Regex("[^+\\d]"), "") // Очистка телефона
                val cleanPhone = formatPhoneNumber(phone.second)
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

    private fun formatPhoneNumber(phone: String): String {
        val digits = phone.replace(Regex("[^\\d]"), "")
        return digits.replace(Regex("(\\d)(\\d{3})(\\d{3})(\\d{2})(\\d{2})"), "+$1 ($2) $3-$4-$5")
    }

}
