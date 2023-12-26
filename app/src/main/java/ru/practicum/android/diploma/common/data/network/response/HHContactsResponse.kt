package ru.practicum.android.diploma.common.data.network.response

import ru.practicum.android.diploma.common.data.dto.ContactsDto
import ru.practicum.android.diploma.common.data.dto.Response

data class HHContactsResponse(
    val contacts: ContactsDto
) : Response()
