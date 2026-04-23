package com.waiter.Controllers

import com.waiter.Models.MejaModel
import com.waiter.Services.Client


class MejaController {
    private val service = Client.meja

    suspend fun getMeja(): List<MejaModel>? {
        return try{
            val response = service.getMeja()
            if (response.isSuccessful) response.body()else null
        }catch (e: Exception){
            null
        }
    }
    suspend fun createMeja(meja: MejaModel): Boolean{
        return try{
            val response = service.createMeja(meja)
            response.isSuccessful
        }catch (e: Exception){
            false
        }
    }
    suspend fun deleteMeja(id: Int): Boolean{
        return try {
            val response = service.deleteMeja(id)
            response.isSuccessful
        }catch (e: Exception) {
            false
        }
    }

}
