package com.waiter.Models


import com.google.gson.annotations.SerializedName

data class Login(
    @SerializedName("name") val Name:String,
    @SerializedName("password")  val Password:String
)

data class LoginFeedback(
    @SerializedName("id") val Id: Int,
    @SerializedName("name") val Name: String,
    @SerializedName("password") val Password: String,
    @SerializedName("roleName") val Role: String,
    @SerializedName("roleId") val RoleId: Int
)

data class UserRequest(
    @SerializedName("name") val name: String,
    @SerializedName("password") val password: String,
    @SerializedName("roleId") val roleId: Int
)

