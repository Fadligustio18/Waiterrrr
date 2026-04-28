package com.waiter.Controllers

import com.waiter.Models.OrderDetailResponse
import com.waiter.Models.OrderListItem
import com.waiter.Models.OrderRequest
import com.waiter.Services.Client
import retrofit2.Response

class OrderControllers {
    private val service = Client.order

    suspend fun createOrder(order: OrderRequest): Response<Boolean> {
        return service.createOrder(order)
    }

    suspend fun getAllOrders(): Response<List<OrderListItem>> {
        return service.getAllOrders()
    }

    // Sekarang hanya butuh statusId sesuai Swagger terbaru
    suspend fun getOrdersByStatus(statusId: Int): Response<List<OrderListItem>> {
        return service.getOrdersByStatus(statusId)
    }

    suspend fun updateOrderStatus(orderId: Int, statusId: Int): Response<Void> {
        return service.updateOrderStatus(orderId, statusId)
    }

    suspend fun getOrderDetailById(orderId: Int): Response<OrderDetailResponse> {
        return service.getOrderById(orderId)
    }
}
