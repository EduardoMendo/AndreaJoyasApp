package com.example.andreajoyas.viewmodel

import androidx.compose.runtime.mutableStateListOf
import androidx.lifecycle.ViewModel
import com.example.andreajoyas.models.Product
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase

class ProductViewModel : ViewModel() {
    var productList = mutableStateListOf<Product>()
        private set

    private val db = Firebase.firestore

    fun addProduct(product: Product) {
        productList.add(product)
    }

    fun deleteProductFromFirebase(product: Product) {
        db.collection("productos").document(product.id).delete()
            .addOnSuccessListener {
                productList.remove(product)
            }
    }

    fun fetchProductsFromFirebase() {
        db.collection("productos")
            .get()
            .addOnSuccessListener { result ->
                productList.clear()
                for (document in result) {
                    val product = document.toObject(Product::class.java).copy(id = document.id)
                    productList.add(product)
                }
            }
    }
}
