package com.example.andreajoyas.screen

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.andreajoyas.model.CarritoItem
import com.example.andreajoyas.ui.theme.DoradoElegante
import com.example.andreajoyas.ui.theme.FondoSuave
import com.example.andreajoyas.viewmodel.CarritoViewModel

@Composable
fun CarritoScreen(
    carritoViewModel: CarritoViewModel = viewModel(),
    onBack: () -> Unit,
    onPagar: () -> Unit
) {
    val carrito by carritoViewModel.carritoItems.collectAsState()
    val total by carritoViewModel.total.collectAsState()

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(FondoSuave)
            .padding(16.dp)
    ) {
        Text("Mi Carrito", style = MaterialTheme.typography.headlineSmall)

        Spacer(modifier = Modifier.height(12.dp))

        LazyColumn(modifier = Modifier.weight(1f)) {
            items(carrito) { item ->
                CarritoItemCard(
                    item = item,
                    onCantidadCambiada = { nuevaCantidad ->
                        carritoViewModel.cambiarCantidad(item.producto.id, nuevaCantidad)
                    },
                    onEliminar = {
                        carritoViewModel.eliminarDelCarrito(item.producto.id)
                    }
                )
            }
        }

        Spacer(modifier = Modifier.height(12.dp))

        Text("Total: C$${"%.2f".format(total)}", style = MaterialTheme.typography.titleMedium)

        Spacer(modifier = Modifier.height(12.dp))

        Button(
            onClick = onPagar,
            modifier = Modifier.fillMaxWidth(),
            colors = ButtonDefaults.buttonColors(containerColor = DoradoElegante)
        ) {
            Text("Pagar")
        }

        Spacer(modifier = Modifier.height(8.dp))

        OutlinedButton(
            onClick = onBack,
            modifier = Modifier.fillMaxWidth(),
            colors = ButtonDefaults.outlinedButtonColors(containerColor = Color.White)
        ) {
            Text("Volver")
        }
    }
}

@Composable
fun CarritoItemCard(
    item: CarritoItem,
    onCantidadCambiada: (Int) -> Unit,
    onEliminar: () -> Unit
) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 6.dp),
        shape = RoundedCornerShape(12.dp),
        colors = CardDefaults.cardColors(containerColor = Color.White),
        elevation = CardDefaults.cardElevation(defaultElevation = 4.dp)
    ) {
        Row(
            modifier = Modifier
                .padding(12.dp)
                .fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Column(modifier = Modifier.weight(1f)) {
                Text(text = item.producto.name, style = MaterialTheme.typography.titleMedium)
                Text(
                    text = "C$${item.producto.price} x ${item.cantidad}",
                    color = DoradoElegante
                )
            }

            Row(verticalAlignment = Alignment.CenterVertically) {
                IconButton(onClick = {
                    onCantidadCambiada(item.cantidad - 1)
                }) {
                    Text("-")
                }

                Text(text = "${item.cantidad}", modifier = Modifier.padding(horizontal = 8.dp))

                IconButton(onClick = {
                    onCantidadCambiada(item.cantidad + 1)
                }) {
                    Text("+")
                }

                IconButton(onClick = onEliminar) {
                    Icon(Icons.Default.Delete, contentDescription = "Eliminar")
                }
            }
        }
    }
}
