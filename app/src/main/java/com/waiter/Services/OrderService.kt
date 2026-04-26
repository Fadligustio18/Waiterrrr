package com.waiter.Services

import com.waiter.Models.OrderDetailResponse
import com.waiter.Models.OrderListItem
import com.waiter.Models.OrderRequest
import com.waiter.Models.OrderStatusRequest
import okhttp3.ResponseBody
import retrofit2.Response
import retrofit2.http.*

interface OrderService {
    @POST("api/order")
    suspend fun createOrder(@Body order: OrderRequest): Response<ResponseBody>

    @GET("api/order/{id}")
    suspend fun getOrderById(@Path("id") orderId: Int): Response<OrderDetailResponse>

    @GET("api/order/status/{id}")
    suspend fun getOrderByStatus(@Path("id") statusId: Int): Response<List<OrderListItem>>

    @PATCH("api/order/{id}/status")
    suspend fun updateOrderStatus(@Path("id") orderId: Int, @Query("statusId") statusId: Int): Response<ResponseBody>
}
