package com.waiter.Services

import com.waiter.Models.MejaModel
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.DELETE
import retrofit2.http.GET
import retrofit2.http.POST

import retrofit2.http.Path

interface MejaServices {
    @GET("api/location")
    suspend fun getMeja(): Response<List<MejaModel>>

    @POST("api/location")
    suspend fun createMeja(@Body meja: MejaModel): Response<Unit>



    @DELETE("api/location/{id}")
    suspend fun deleteMeja(@Path("id") id: Int): Response<Unit>
}
