package com.waiter.Controllers

import com.waiter.Models.OrderDetailResponse
import com.waiter.Models.OrderListItem
import com.waiter.Models.OrderRequest
import com.waiter.Services.Client
import okhttp3.ResponseBody
import retrofit2.Response

class OrderControllers {
    private val service = Client.order

    suspend fun createOrder(order: OrderRequest): Response<ResponseBody> {
        return service.createOrder(order)
    }

    suspend fun getOrderById(orderId: Int): Response<OrderDetailResponse> {
        return service.getOrderById(orderId)
    }

    suspend fun getOrdersByStatus(statusId: Int): Response<List<OrderListItem>> {
        return service.getOrderByStatus(statusId)
    }

    suspend fun updateOrderStatus(orderId: Int, statusId: Int): Response<ResponseBody> {
        return service.updateOrderStatus(orderId, statusId)
    }
}