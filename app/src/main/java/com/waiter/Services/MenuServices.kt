package com.waiter.Services

import okhttp3.MultipartBody
import okhttp3.RequestBody
import okhttp3.ResponseBody
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.DELETE
import retrofit2.http.GET
import retrofit2.http.Multipart
import retrofit2.http.POST
import retrofit2.http.PUT
import retrofit2.http.Part
import retrofit2.http.Path

interface MenuServices {
    @Multipart
    @POST("api/menu")
    suspend fun createMenu(
        @Part("Name") name: RequestBody,
        @Part("Price") price: RequestBody,
        @Part("TypeId") typeId: RequestBody,
        @Part("TypeName") typeName: RequestBody,
        @Part image: MultipartBody.Part
    ): Response<ResponseBody>

    @DELETE("api/menu/{id}")
    suspend fun deleteMenu(@Path("id") id:Int): Response<Unit>

    @GET("api/menu")
    suspend fun getMenu(): Response<List<com.waiter.Models.MenuResponse>>

    @PUT("api/menu/name/{id}")
    suspend fun updateMenu(@Path("id") id : Int, @Body menu: com.waiter.Models.Menu): Response<Void>

    @PUT("api/menu/price/{id}")
    suspend fun updateMenuPrice(@Path("id") id : Int, @Body menu: com.waiter.Models.Menu): Response<Void>



}