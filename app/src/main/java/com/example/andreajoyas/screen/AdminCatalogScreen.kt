package com.example.andreajoyas.screen

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import coil.compose.rememberAsyncImagePainter
import com.example.andreajoyas.models.Product
import com.example.andreajoyas.viewmodel.ProductViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AdminCatalogScreen(
    navController: NavController,
    viewModel: ProductViewModel = viewModel()
) {
    LaunchedEffect(true) {
        viewModel.fetchProductsFromFirebase()
    }

    val products = viewModel.productList

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Administrar Catálogo") },
                navigationIcon = {
                    IconButton(onClick = { navController.popBackStack() }) {
                        Icon(Icons.Filled.ArrowBack, contentDescription = "Regresar")
                    }
                }
            )
        }
    ) { padding ->
        LazyColumn(
            modifier = Modifier
                .padding(padding)
                .padding(16.dp)
        ) {
            items(products) { product ->
                ProductItemAdmin(
                    product = product,
                    onEdit = {
                        navController.navigate("edit_product/${product.id}")
                    },
                    onDelete = {
                        viewModel.deleteProductFromFirebase(product)
                    }
                )
            }
        }
    }
}

@Composable
fun ProductItemAdmin(
    product: Product,
    onEdit: () -> Unit,
    onDelete: () -> Unit
) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 8.dp),
        elevation = CardDefaults.cardElevation(4.dp)
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            if (product.imageUrl.isNotBlank()) {
                Image(
                    painter = rememberAsyncImagePainter(product.imageUrl),
                    contentDescription = "Imagen del producto",
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(180.dp),
                    contentScale = ContentScale.Crop
                )
                Spacer(modifier = Modifier.height(8.dp))
            }

            Text("Nombre: ${product.name}", style = MaterialTheme.typography.titleMedium)
            Text("Precio: $${product.price}", style = MaterialTheme.typography.bodyMedium)
            Spacer(modifier = Modifier.height(8.dp))

            Row(
                horizontalArrangement = Arrangement.SpaceBetween,
                modifier = Modifier.fillMaxWidth()
            ) {
                Button(onClick = onEdit) {
                    Text("Editar")
                }
                Button(
                    onClick = onDelete,
                    colors = ButtonDefaults.buttonColors(
                        containerColor = Color.Red,
                        contentColor = Color.White
                    )
                ) {
                    Text("Eliminar")
                }
            }
        }
    }
}
