package com.waiter.Models

import com.google.gson.annotations.SerializedName

data class OrderListItem(
    @SerializedName(value = "id", alternate = ["Id"]) val id: Int,
    @SerializedName(value = "userId", alternate = ["UserId"]) val userId: Int,
    @SerializedName(value = "customerName", alternate = ["CustomerName"]) val customerName: String? = "",
    @SerializedName(value = "date", alternate = ["Date"]) val date: String? = "",
    @SerializedName(value = "statusId", alternate = ["StatusId"]) val statusId: Int,
    @SerializedName(value = "locationId", alternate = ["LocationId"]) val locationId: Int,
    @SerializedName(value = "userName", alternate = ["UserName", "WaiterName", "waiterName"]) val userName: String? = "",
    @SerializedName(value = "statusName", alternate = ["StatusName"]) val statusName: String? = "",
    @SerializedName(value = "locationName", alternate = ["LocationName", "TableName", "tableName"]) val locationName: String? = ""
)

data class OrderDetailResponse(
    @SerializedName(value = "order", alternate = ["Order"]) val order: OrderListItem,
    @SerializedName(value = "items", alternate = ["Items"]) val items: List<OrderItemDetail>
)

data class OrderItemDetail(
    @SerializedName(value = "id", alternate = ["Id"]) val id: Int,
    @SerializedName(value = "orderId", alternate = ["OrderId"]) val orderId: Int,
    @SerializedName(value = "menuId", alternate = ["MenuId"]) val menuId: Int,
    @SerializedName(value = "quantity", alternate = ["Quantity"]) val quantity: Int,
    @SerializedName(value = "priceAtOrder", alternate = ["PriceAtOrder"]) val priceAtOrder: Int,
    @SerializedName(value = "menuName", alternate = ["MenuName"]) val menuName: String? = ""
)

data class OrderStatusRequest(
    @SerializedName("statusId") val statusId: Int
)
