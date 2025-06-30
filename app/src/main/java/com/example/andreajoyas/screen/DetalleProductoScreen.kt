package com.example.andreajoyas.screen

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Remove
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.andreajoyas.ui.theme.DoradoElegante
import com.example.andreajoyas.ui.theme.FondoSuave
import com.example.andreajoyas.viewmodel.CarritoViewModel
import com.google.firebase.firestore.FirebaseFirestore

@Composable
fun DetalleProductoScreen(
    productoId: String,
    onBack: () -> Unit,
    carritoViewModel: CarritoViewModel = viewModel()
) {
    var producto by remember { mutableStateOf<Producto?>(null) }
    var cantidad by remember { mutableStateOf(1) }

    // ✅ Cargar datos reales del producto desde Firestore
    LaunchedEffect(productoId) {
        FirebaseFirestore.getInstance()
            .collection("productos")
            .document(productoId)
            .get()
            .addOnSuccessListener { document ->
                if (document.exists()) {
                    val p = document.toObject(Producto::class.java)
                    producto = p?.copy(id = document.id)
                }
            }
            .addOnFailureListener {
                // Opcional: manejo de error
            }
    }

    producto?.let { p ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(FondoSuave)
                .padding(24.dp)
        ) {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .wrapContentHeight()
                    .align(Alignment.Center),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Text(text = p.name, style = MaterialTheme.typography.titleLarge)
                Spacer(modifier = Modifier.height(12.dp))
                Text(text = "C$${p.price}", color = DoradoElegante, style = MaterialTheme.typography.titleMedium)
                Spacer(modifier = Modifier.height(12.dp))
                Text(
                    text = p.description,
                    style = MaterialTheme.typography.bodyMedium,
                    modifier = Modifier.padding(horizontal = 12.dp)
                )
                Spacer(modifier = Modifier.height(24.dp))

                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.Center
                ) {
                    Text("Cantidad:", style = MaterialTheme.typography.bodyMedium)
                    Spacer(modifier = Modifier.width(12.dp))
                    IconButton(
                        onClick = { if (cantidad > 1) cantidad-- },
                        modifier = Modifier.size(40.dp)
                    ) {
                        Icon(Icons.Default.Remove, contentDescription = "Disminuir")
                    }
                    Text("$cantidad", style = MaterialTheme.typography.bodyLarge)
                    IconButton(
                        onClick = { if (cantidad < p.stock) cantidad++ },
                        modifier = Modifier.size(40.dp)
                    ) {
                        Icon(Icons.Default.Add, contentDescription = "Aumentar")
                    }
                }

                Spacer(modifier = Modifier.height(24.dp))

                Button(
                    onClick = {
                        repeat(cantidad) {
                            carritoViewModel.agregarAlCarrito(p)
                        }
                    },
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(vertical = 8.dp),
                    colors = ButtonDefaults.buttonColors(containerColor = DoradoElegante),
                    shape = RoundedCornerShape(20.dp)
                ) {
                    Text("Agregar al carrito", color = Color.White)
                }

                Button(
                    onClick = onBack,
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(vertical = 4.dp),
                    colors = ButtonDefaults.buttonColors(containerColor = Color.LightGray),
                    shape = RoundedCornerShape(20.dp)
                ) {
                    Text("Volver")
                }
            }
        }
    }
}
