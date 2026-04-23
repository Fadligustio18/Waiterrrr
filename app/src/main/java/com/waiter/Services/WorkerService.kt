package com.waiter.Services

import com.waiter.Models.Worker
import retrofit2.http.DELETE
import retrofit2.http.GET
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.PUT
import retrofit2.http.Path

interface WorkerService{
    @GET("api/user")
    suspend fun Worker_Services(): Response<List<Worker>>

    @PUT("api/user/{id}")
    suspend fun updateWorker(@Path("id") id: Int, @Body worker: Worker): Response<Void>

    @DELETE("api/user/{id}")
    suspend fun deleteWorker(@Path("id") id: Int): Response<Void>
}