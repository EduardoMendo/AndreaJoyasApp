package com.example.andreajoyas.model

import com.example.andreajoyas.screen.Producto

data class CarritoItem(
    val producto: Producto,
    var cantidad: Int = 1
)