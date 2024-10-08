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

//    @GET("events/{id}")
//    fun getDetailEvent(
//        @Path("id") id: String
//    ): Call<DetailDicodingEventResponse>

    @GET("events")
    fun searchEvents(
        @Query("active") active: Int,
        @Query("q") query: String
    ): Call<DicodingEventResponse>
}