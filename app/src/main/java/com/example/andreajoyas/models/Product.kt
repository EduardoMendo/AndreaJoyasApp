package com.example.andreajoyas.models

data class Product(
    val id: String = "",
    val name: String = "",
    val description: String = "",
    val price: Double = 0.0,
    val category: String = "",
    val stock: Int = 0,
    val imageUrl: String = ""
)
