package com.waiter.Services

import com.waiter.Models.OrderDetailResponse
import com.waiter.Models.OrderListItem
import com.waiter.Models.OrderRequest
import retrofit2.Response
import retrofit2.http.*

interface OrderService {
    // 1. POST /api/order
    @POST("api/order")
    suspend fun createOrder(@Body orderRequest: OrderRequest): Response<Boolean>

    // 2. GET /api/order
    @GET("api/order")
    suspend fun getAllOrders(): Response<List<OrderListItem>>

    // 3. GET /api/order/status/{statusId}
    @GET("api/order/status/{statusId}")
    suspend fun getOrdersByStatus(
        @Path("statusId") statusId: Int
    ): Response<List<OrderListItem>>

    // 4. PATCH /api/order/{id}/status/{statusId}
    @PATCH("api/order/{id}/status/{statusId}")
    suspend fun updateOrderStatus(
        @Path("id") id: Int,
        @Path("statusId") statusId: Int
    ): Response<Void>

    // 5. GET /api/order/{id}
    @GET("api/order/{id}")
    suspend fun getOrderById(@Path("id") id: Int): Response<OrderDetailResponse>
}
