package com.waiter.Models

data class CartItem(
    val menu: MenuResponse,
    var quantity: Int = 1
)