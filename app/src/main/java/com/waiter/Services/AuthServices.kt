package com.waiter.Services

import com.waiter.Models.Login
import com.waiter.Models.LoginFeedback
import retrofit2.http.Body
import retrofit2.http.POST
import retrofit2.Response

interface AuthServices {
    @POST("api/auth/login")
    suspend fun Login_Services(@Body login: Login) : Response<LoginFeedback>

    @POST("api/user")
    suspend fun Register_Services(@Body user: com.waiter.Models.UserRequest) : Response<Unit>
}