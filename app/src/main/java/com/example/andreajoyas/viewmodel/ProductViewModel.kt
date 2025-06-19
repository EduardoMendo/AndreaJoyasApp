package com.example.andreajoyas.viewmodel

import androidx.compose.runtime.mutableStateListOf
import androidx.lifecycle.ViewModel
import com.example.andreajoyas.models.Product
import java.util.*

class ProductViewModel : ViewModel() {
    var productList = mutableStateListOf<Product>()
        private set

    fun addProduct(product: Product) {
        productList.add(product)
    }
}
