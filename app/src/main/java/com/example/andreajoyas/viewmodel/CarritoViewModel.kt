package com.example.andreajoyas.viewmodel

import androidx.lifecycle.ViewModel
import com.example.andreajoyas.model.CarritoItem
import com.example.andreajoyas.screen.Producto
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.update

class CarritoViewModel : ViewModel() {

    private val _carritoItems = MutableStateFlow<List<CarritoItem>>(emptyList())
    val carritoItems: StateFlow<List<CarritoItem>> = _carritoItems

    private val _total = MutableStateFlow(0.0)
    val total: StateFlow<Double> = _total

    fun agregarAlCarrito(producto: Producto) {
        _carritoItems.update { currentItems ->
            val existente = currentItems.find { it.producto.id == producto.id }
            if (existente != null) {
                currentItems.map {
                    if (it.producto.id == producto.id) it.copy(cantidad = it.cantidad + 1) else it
                }
            } else {
                currentItems + CarritoItem(producto = producto)
            }
        }
        actualizarTotal()
    }

    fun eliminarDelCarrito(productoId: String) {
        _carritoItems.update { it.filter { item -> item.producto.id != productoId } }
        actualizarTotal()
    }

    fun cambiarCantidad(productoId: String, nuevaCantidad: Int) {
        if (nuevaCantidad <= 0) {
            eliminarDelCarrito(productoId)
            return
        }

        _carritoItems.update { currentItems ->
            currentItems.map {
                if (it.producto.id == productoId) it.copy(cantidad = nuevaCantidad) else it
            }
        }
        actualizarTotal()
    }

    private fun actualizarTotal() {
        val totalCalculado = _carritoItems.value.sumOf { it.producto.price * it.cantidad.toDouble() }
        _total.value = totalCalculado
    }

    fun obtenerTotal(): Double {
        return _carritoItems.value.sumOf { it.producto.price * it.cantidad.toDouble() }
    }

    fun vaciarCarrito() {
        _carritoItems.value = emptyList()
        _total.value = 0.0
    }
}
