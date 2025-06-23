package com.example.andreajoyas.screen

import android.net.Uri
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.unit.dp
import coil.compose.rememberAsyncImagePainter
import com.example.andreajoyas.models.Product
import com.example.andreajoyas.ui.theme.DoradoElegante
import com.example.andreajoyas.viewmodel.ProductViewModel
import java.util.*
import androidx.navigation.NavController

@Composable
fun AddProductScreen(
    productViewModel: ProductViewModel,
    navController: NavController,
    onProductAdded: () -> Unit
) {
    // Estados del formulario
    var name by remember { mutableStateOf("") }
    var description by remember { mutableStateOf("") }
    var price by remember { mutableStateOf("") }
    var category by remember { mutableStateOf("anillos") }
    var stock by remember { mutableStateOf("") }
    var imageUri by remember { mutableStateOf<Uri?>(null) }
    var showSuccess by remember { mutableStateOf(false) }
    var showError by remember { mutableStateOf<String?>(null) }

    val categories = listOf("anillos", "cadenas", "brazaletes", "aretes")
    val imagePicker = rememberLauncherForActivityResult(ActivityResultContracts.GetContent()) { uri ->
        imageUri = uri
    }

    // Diálogos de alerta
    if (showSuccess) {
        AlertDialog(
            onDismissRequest = { showSuccess = false },
            title = { Text("Producto agregado") },
            text = { Text("¡El producto fue agregado exitosamente!") },
            confirmButton = {
                TextButton(onClick = {
                    showSuccess = false
                    onProductAdded()
                }) { Text("OK") }
            }
        )
    }
    showError?.let { err ->
        AlertDialog(
            onDismissRequest = { showError = null },
            title = { Text("Error") },
            text = { Text(err) },
            confirmButton = {
                TextButton(onClick = { showError = null }) { Text("OK") }
            }
        )
    }

    Column(Modifier.fillMaxSize()) {
        // TopAppBar con botón de retroceso
        TopAppBar(
            title = { Text("Agregar producto") },
            navigationIcon = {
                IconButton(onClick = { navController.popBackStack() }) {
                    Icon(Icons.Filled.ArrowBack, contentDescription = "Volver al menú")
                }
            },
            backgroundColor = DoradoElegante,
            contentColor = Color.White,
            elevation = 4.dp
        )

        // Formulario
        Column(Modifier.padding(16.dp)) {
            OutlinedTextField(
                value = name, onValueChange = { name = it },
                label = { Text("Nombre") }, modifier = Modifier.fillMaxWidth()
            )
            Spacer(Modifier.height(8.dp))

            OutlinedTextField(
                value = description, onValueChange = { description = it },
                label = { Text("Descripción") }, modifier = Modifier.fillMaxWidth()
            )
            Spacer(Modifier.height(8.dp))

            OutlinedTextField(
                value = price,
                onValueChange = { if (it.all { c -> c.isDigit() || c == '.' }) price = it },
                label = { Text("Precio") },
                modifier = Modifier.fillMaxWidth()
            )
            Spacer(Modifier.height(8.dp))

            // Categoría (dropdown simple)
            DropdownMenuCategory(
                selected = category,
                options = categories,
                onSelect = { category = it }
            )
            Spacer(Modifier.height(8.dp))

            OutlinedTextField(
                value = stock,
                onValueChange = { if (it.all { c -> c.isDigit() }) stock = it },
                label = { Text("Stock/Cantidad") },
                modifier = Modifier.fillMaxWidth()
            )
            Spacer(Modifier.height(16.dp))

            // Botón “Seleccionar imagen”
            Button(
                onClick = { imagePicker.launch("image/*") },
                modifier = Modifier.fillMaxWidth(),
                colors = ButtonDefaults.buttonColors(backgroundColor = DoradoElegante)
            ) {
                Text("Seleccionar imagen (opcional)", color = Color.White)
            }
            Spacer(Modifier.height(16.dp))

            // Botón “Guardar producto”
            Button(
                onClick = {
                    try {
                        val prod = Product(
                            id = UUID.randomUUID().toString(),
                            name = name,
                            description = description,
                            price = price.toDoubleOrNull() ?: 0.0,
                            category = category,
                            imageUri = imageUri?.toString(),
                            stock = stock.toIntOrNull() ?: 0
                        )
                        productViewModel.addProduct(prod)
                        showSuccess = true
                        // Limpiar campos
                        name = ""
                        description = ""
                        price = ""
                        stock = ""
                        category = categories.first()
                        imageUri = null
                    } catch (e: Exception) {
                        showError = e.localizedMessage
                    }
                },
                enabled = name.isNotBlank() && description.isNotBlank() && price.isNotBlank(),
                modifier = Modifier.fillMaxWidth(),
                colors = ButtonDefaults.buttonColors(backgroundColor = DoradoElegante)
            ) {
                Text("Guardar producto", color = Color.White)
            }
        }
    }
}

// DropdownCategory (igual que antes)
@Composable
fun DropdownMenuCategory(
    selected: String,
    options: List<String>,
    onSelect: (String) -> Unit
) {
    var expanded by remember { mutableStateOf(false) }
    Column {
        OutlinedTextField(
            value = selected, onValueChange = {},
            label = { Text("Categoría") }, readOnly = true,
            modifier = Modifier
                .fillMaxWidth()
                .clickable { expanded = true }
        )
        DropdownMenu(expanded = expanded, onDismissRequest = { expanded = false }) {
            options.forEach { cat ->
                DropdownMenuItem(onClick = {
                    onSelect(cat)
                    expanded = false
                }) {
                    Text(cat.replaceFirstChar { it.uppercase() })
                }
            }
        }
    }
}
