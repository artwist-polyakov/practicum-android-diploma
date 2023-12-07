package ru.practicum.android.diploma.search.ui.viewmodels.states

import ru.practicum.android.diploma.R

enum class ErrorsSearchScreenStates(val messageResource: Int, val imageResource: Int) {
    EMPTY(0, R.drawable.image_search),
    NO_INTERNET(R.string.no_internet, R.drawable.image_scull),
    NOT_FOUND(R.string.no_vacancies, R.drawable.image_cat),
    FAIL_FETCH_REGIONS(R.string.region_empty_list, R.drawable.image_fly),
    NO_REGION(R.string.region_error, R.drawable.image_cat),
    SERVER_ERROR(R.string.server_error, R.drawable.image_sad),
}
