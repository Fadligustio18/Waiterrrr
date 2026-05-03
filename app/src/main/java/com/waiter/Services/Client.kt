package com.waiter.Services
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import kotlin.getValue
import kotlin.jvm.java


object Client {
    // UBAH IP DI SINI SAJA (Tanpa Spasi)
    const val BASE_URL = "http://192.168.1.7:3000/"

    private val http by lazy {
        Retrofit.Builder()
            .baseUrl(BASE_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
    }

    val auth: AuthServices by lazy {
        http.create(AuthServices::class.java)
    }
   val worker: WorkerService by lazy {
       http.create(WorkerService::class.java)
   }
   val menu: MenuServices by lazy {
       http.create(MenuServices::class.java)
   }
   val meja: MejaServices by lazy {
       http.create(MejaServices::class.java)
   }
    val order: OrderService by lazy {
        http.create(OrderService::class.java)
    }
}