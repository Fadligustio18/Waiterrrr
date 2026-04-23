package com.waiter.Controllers

import com.waiter.Models.Worker
import com.waiter.Services.Client
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

class WorkerControllers {
    private val service = Client.worker
    suspend fun GetWorker(): List<Worker>? = withContext(Dispatchers.IO){
        try {
            val response = service.Worker_Services()
            if(response.isSuccessful){
                return@withContext response.body()
            }else{
                return@withContext null
            }
        }catch (e:Exception){
            return@withContext null
        }
    }

    suspend fun updateWorker(id: Int, worker: Worker): Boolean = withContext(Dispatchers.IO) {
        try {
            val response = service.updateWorker(id, worker)
            response.isSuccessful
        } catch (e: Exception) {
            false
        }
    }

    suspend fun deleteWorker(id: Int): Boolean = withContext(Dispatchers.IO) {
        try {
            val response = service.deleteWorker(id)
            response.isSuccessful
        } catch (e: Exception) {
            false
        }
    }
}