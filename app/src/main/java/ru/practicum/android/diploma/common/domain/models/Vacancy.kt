package ru.practicum.android.diploma.common.domain.models

data class Vacancy (
    val vacancyId: Int,
    val city:String,
    val jobDescription: String?,
    val jobTiming: String?, //например, "Полная занятость"
    val experience: String?, //например, "От 1 года до 3 лет"
    val keySkills: List<String>?,  //их может быть несколько, у каждого поле name
    val professional_roles: List<String>?, //тоже несколько
    val jobName: String,
    //зарплата:
    val currency: String,
    val from: String,
    val to: String,
    val schedule: String?, //например, "Полный день"
    val employerId: Int,
    val employerEmail: String?,
    val employerContactName: String?,
    val employerPhone: List<String>?,
    val comment:String?, //комментарий из раздела Контакты
    val employerLogo_urls_90: String?,
    val employerLogo_urls_240: String?,
    val employerLogo_urls_original: String?,
    val employerName: String,
)
