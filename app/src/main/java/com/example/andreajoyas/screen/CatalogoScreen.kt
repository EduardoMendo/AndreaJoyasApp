package com.example.andreajoyas.screen

import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import coil.compose.rememberAsyncImagePainter
import coil.request.ImageRequest
import com.example.andreajoyas.ui.theme.DoradoElegante
import com.google.firebase.firestore.FirebaseFirestore

data class Producto(
    val id: String = "",
    val name: String = "",
    val description: String = "",
    val price: Int = 0,
    val stock: Int = 0,
    val category: String = "",
    val imageUri: String = ""
)

@Composable
fun CatalogoScreen(
    navController: NavHostController // ✅ agregado para navegar al detalle
) {
    var productos by remember { mutableStateOf<List<Producto>>(emptyList()) }

    LaunchedEffect(Unit) {
        FirebaseFirestore.getInstance()
            .collection("productos")
            .get()
            .addOnSuccessListener { result ->
                productos = result.documents.mapNotNull { doc ->
                    doc.toObject(Producto::class.java)
                }
            }
    }

    LazyColumn(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
    ) {
        items(productos) { producto ->
            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 8.dp)
                    .clickable {
                        // ✅ ahora navega al detalle con el ID del producto
                        navController.navigate("detalle/${producto.id}")
                    },
                shape = RoundedCornerShape(12.dp),
                colors = CardDefaults.cardColors(containerColor = Color.White),
                elevation = CardDefaults.cardElevation(defaultElevation = 6.dp)
            ) {
                Row(
                    modifier = Modifier
                        .padding(16.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    if (producto.imageUri.isNotBlank()) {
                        Image(
                            painter = rememberAsyncImagePainter(
                                ImageRequest.Builder(LocalContext.current)
                                    .data(producto.imageUri)
                                    .crossfade(true)
                                    .build()
                            ),
                            contentDescription = producto.name,
                            modifier = Modifier
                                .size(80.dp)
                                .padding(end = 16.dp),
                            contentScale = ContentScale.Crop
                        )
                    }

                    Column {
                        Text(text = producto.name, style = MaterialTheme.typography.titleMedium)
                        Text(text = "C$${producto.price}", color = DoradoElegante)
                    }
                }
            }
        }
    }
}
