package com.waiter.Models

import com.google.gson.annotations.SerializedName

data class MenuResponse(
    @SerializedName("id") val id: Int,
    @SerializedName("name", alternate = ["Name"]) val name: String,
    @SerializedName("price", alternate = ["Price"]) val price: Int,
    @SerializedName("typeId", alternate = ["type_id"]) val typeId: Int,
    @SerializedName("typeName", alternate = ["type_name"]) val typeName: String,
    @SerializedName("imageUrl", alternate = ["image_url", "image"]) val imageUrl: String?
)