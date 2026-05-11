package com.waiter.Controllers

import android.content.Context
import android.net.Uri
import com.waiter.Services.Client
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.MultipartBody
import okhttp3.RequestBody.Companion.asRequestBody
import okhttp3.RequestBody.Companion.toRequestBody
import java.io.File
import java.io.FileOutputStream

class MenuControllers {
    private val service = Client.menu

    suspend fun createMenu(
        context: Context,
        name: String,
        price: String,
        typeId: String,
        typeName: String,
        imageUri: Uri
    ): Boolean = withContext(Dispatchers.IO) {
        try {
            val file = uriToFile(context, imageUri) ?: return@withContext false
            val requestFile = file.asRequestBody("image/*".toMediaTypeOrNull())
            val imagePart = MultipartBody.Part.createFormData("image", file.name, requestFile)

            val nameBody = name.toRequestBody("text/plain".toMediaTypeOrNull())
            val priceBody = price.toRequestBody("text/plain".toMediaTypeOrNull())
            val typeIdBody = typeId.toRequestBody("text/plain".toMediaTypeOrNull())
            val typeNameBody = typeName.toRequestBody("text/plain".toMediaTypeOrNull())

            val response = service.createMenu(nameBody, priceBody, typeIdBody, typeNameBody, imagePart)
            response.isSuccessful  // ← Cukup cek isSuccessful saja
        } catch (e: Exception) {
            android.util.Log.e("MenuControllers", "Exception: ${e.message}", e)
            false
        }
    }

    private fun uriToFile(context: Context, uri: Uri): File? {
        val inputStream = context.contentResolver.openInputStream(uri) ?: return null
        val file = File(context.cacheDir, "temp_image_${System.currentTimeMillis()}.jpg")
        val outputStream = FileOutputStream(file)
        inputStream.copyTo(outputStream)
        inputStream.close()
        outputStream.close()
        return file
    }

    suspend fun deleteMenu(id:Int): Boolean? = withContext(Dispatchers.IO){
        try {
            val response = service.deleteMenu(id)
            response.isSuccessful
        }catch (e:Exception){
            false
        }
    }

    suspend fun getMenu(): List<com.waiter.Models.MenuResponse>? = withContext(Dispatchers.IO) {
        try {
            val response = service.getMenu()
            if (response.isSuccessful) response.body() else null
        } catch (e: Exception) {
            e.printStackTrace()
            null
        }
    }

    suspend fun updateMenuName(id: Int, menu: com.waiter.Models.Menu): Boolean = withContext(Dispatchers.IO) {
        try {
            val response = service.updateMenu(id, menu)
            response.isSuccessful
        } catch (e: Exception) {
            false
        }
    }

    suspend fun updateMenuPrice(id: Int, menu: com.waiter.Models.Menu): Boolean = withContext(Dispatchers.IO) {
        try {
            val response = service.updateMenuPrice(id, menu)
            response.isSuccessful
        } catch (e: Exception) {
            false
        }
    }
}
