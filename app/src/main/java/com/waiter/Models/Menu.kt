package com.waiter.Models

import android.net.Uri

data class Menu(
    val id: String,
    val name: String,
    val price: String,
    val category: String,
    val imageUri: Uri? = null,
    val imageUrl: String? = null
)