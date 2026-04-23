package com.waiter.Models

data class MenuResponse(
    val id: Int,
    val name: String,
    val price: Int,
    val typeId: Int,
    val typeName: String,
    val imageUrl: String?
)