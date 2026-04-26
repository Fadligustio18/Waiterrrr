package com.waiter.Models

data class OrderRequest(
    val order: OrderDetails,
    val items: List<OrderItemDetails>
)

data class OrderDetails(
    val id: Int? = 0,
    val userId: Int,
    val customerName: String,
    val date: String? = "",
    val statusId: Int,
    val locationId: Int,
    val userName: String? = "",
    val statusName: String? = "",
    val locationName: String? = ""
)

data class OrderItemDetails(
    val id: Int? = 0,
    val orderId: Int? = 0,
    val menuId: Int,
    val quantity: Int,
    val priceAtOrder: Int,
    val menuName: String? = ""
)

// Menjaga kompatibilitas jika ada bagian lain yang menggunakan class Order
data class Order(
    val id: Int,
    val userId: Int,
    val customerName: String,
    val date: String,
    val statusId: Int,
    val locationId: Int,
    val userName: String,
    val locationName: String,
    val statusName: String
)
