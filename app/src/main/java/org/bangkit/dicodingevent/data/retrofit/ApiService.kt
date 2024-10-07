package org.bangkit.dicodingevent.data.retrofit

import org.bangkit.dicodingevent.data.response.DicodingEventResponse
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Query

interface ApiService {
    @GET("events")
    fun getEvents(
        @Query("active") active: Int
    ): Call<DicodingEventResponse>
}