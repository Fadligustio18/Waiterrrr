package com.waiter.Models

import com.google.gson.annotations.SerializedName


data class Worker (
    @SerializedName("id") val id: Int,
    @SerializedName("name") val name: String,
    @SerializedName("password") val password: String,
    @SerializedName("roleName") val roleName: String,
    @SerializedName("roleId") val roleId: Int
)
