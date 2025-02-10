package com.example.sub2dicodingeventfun4

import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query

interface ApiService {
    @GET("events")
    fun getEvents(
        @Query("active") active: Int // Query untuk mendapatkan event yang aktif
    ): Call<EventResponse>

    @GET("events/{id}")
    fun getDetailEvent(
        @Path("id") id: Int
    ): Call<EventDetailResponse>
}