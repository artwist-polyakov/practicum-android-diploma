package ru.practicum.android.diploma.common.data.network

import android.content.Context
import ru.practicum.android.diploma.common.data.dto.Response

class RetrofitNetworkClient(
    private val hhService: HHService,
    private val context: Context
) : NetworkClient {
    override suspend fun doRequest(dto: Any): Response {
        TODO("Not yet implemented")
    }
}
