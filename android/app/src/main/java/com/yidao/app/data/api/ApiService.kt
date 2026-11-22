package com.yidao.app.data.api

import retrofit2.http.Body
import retrofit2.http.POST

interface ApiService {
    @POST("api/v1/triage")
    suspend fun triage(@Body request: TriageRequest): TriageResponse
}
