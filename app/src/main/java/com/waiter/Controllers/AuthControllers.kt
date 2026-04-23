package com.waiter.Controllers

import com.waiter.Models.Login
import com.waiter.Models.LoginFeedback
import com.waiter.Services.Client
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

class AuthControllers {
    private val service = Client.auth
    suspend fun loginController(login : Login): LoginFeedback?= withContext(Dispatchers.IO){
        try {
            val response = service.Login_Services(login)
            if(response.code()==200){
                return@withContext response.body()
            }else{
                return@withContext null
            }
        }catch (e:Exception){
            return@withContext null
        }
    }

    suspend fun registerController(user: com.waiter.Models.UserRequest): Boolean = withContext(Dispatchers.IO) {
        try {
            val response = service.Register_Services(user)
            response.isSuccessful
        } catch (e: Exception) {
            false
        }
    }
}