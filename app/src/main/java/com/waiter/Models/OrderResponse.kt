package com.waiter.Models

import com.google.gson.annotations.SerializedName

data class OrderListItem(
    @SerializedName("Id") val id: Int,
    @SerializedName("UserId") val userId: Int,
    @SerializedName("CustomerName") val customerName: String? = "",
    @SerializedName("Date") val date: String? = "",
    @SerializedName("StatusId") val statusId: Int,
    @SerializedName("LocationId") val locationId: Int,
    @SerializedName("WaiterName") val waiterName: String? = "",
    @SerializedName("StatusName") val statusName: String? = "",
    @SerializedName(value = "TableName", alternate = ["LocationName"]) val tableName: String? = ""
)

data class OrderDetailResponse(
    // Handle "Order" atau "order"
    @SerializedName(value = "Order", alternate = ["order"]) val order: OrderListItem,
    // Handle "Items" atau "items"
    @SerializedName(value = "Items", alternate = ["items"]) val items: List<OrderItemDetail>
)

data class OrderItemDetail(
    @SerializedName("Id") val id: Int,
    @SerializedName("OrderId") val orderId: Int,
    @SerializedName("MenuId") val menuId: Int,
    @SerializedName("Quantity") val quantity: Int,
    @SerializedName("PriceAtOrder") val priceAtOrder: Int,
    @SerializedName("MenuName") val menuName: String? = ""
)

data class OrderStatusRequest(
    val statusId: Int
)
