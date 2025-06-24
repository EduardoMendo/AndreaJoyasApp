package com.example.andreajoyas.screen


import android.net.Uri
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.Image
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
import androidx.compose.ui.Alignment
import androidx.navigation.NavController
import coil.compose.rememberAsyncImagePainter
import com.example.andreajoyas.models.Product
import com.example.andreajoyas.ui.theme.DoradoElegante
import com.google.firebase.firestore.FirebaseFirestore
import java.util.*

@Composable
fun AddProductScreen(
    navController: NavController,
    onProductAdded: () -> Unit
) {
    // --- Estados del formulario ---
    var name by remember { mutableStateOf("") }
    var description by remember { mutableStateOf("") }
    var price by remember { mutableStateOf("") }
    var category by remember { mutableStateOf("anillos") }
    var stock by remember { mutableStateOf("") }
    var imageUri by remember { mutableStateOf<Uri?>(null) }
    var isLoading by remember { mutableStateOf(false) }
    var showError by remember { mutableStateOf<String?>(null) }
    var showSuccess by remember { mutableStateOf(false) }

    val categories = listOf("anillos", "cadenas", "brazaletes", "aretes")

    // Selector de imagen
    val imagePicker = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.GetContent()
    ) { imageUri = it }

    // Instancia de Firestore
    val db = FirebaseFirestore.getInstance().collection("productos")

    // Diálogo de éxito
    if (showSuccess) {
        AlertDialog(
            onDismissRequest = { showSuccess = false },
            title = { Text("Producto agregado") },
            text = { Text("¡Producto guardado en Firestore correctamente!") },
            confirmButton = {
                TextButton(onClick = {
                    showSuccess = false
                    onProductAdded()
                }) { Text("OK") }
            }
        )
    }
    // Diálogo de error
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
        // Barra superior con flecha de retroceso
        TopAppBar(
            title = { Text("Agregar producto") },
            navigationIcon = {
                IconButton(onClick = { navController.popBackStack() }) {
                    Icon(Icons.Filled.ArrowBack, contentDescription = "Volver")
                }
            },
            backgroundColor = DoradoElegante,
            contentColor = Color.White,
            elevation = 4.dp
        )

        // Formulario
        Column(
            Modifier
                .padding(16.dp)
                .fillMaxSize()
        ) {
            OutlinedTextField(
                value = name,
                onValueChange = { name = it },
                label = { Text("Nombre") },
                modifier = Modifier.fillMaxWidth()
            )
            Spacer(Modifier.height(8.dp))

            OutlinedTextField(
                value = description,
                onValueChange = { description = it },
                label = { Text("Descripción") },
                modifier = Modifier.fillMaxWidth()
            )
            Spacer(Modifier.height(8.dp))

            OutlinedTextField(
                value = price,
                onValueChange = { if (it.all { c -> c.isDigit() || c == '.' }) price = it },
                label = { Text("Precio") },
                modifier = Modifier.fillMaxWidth()
            )
            Spacer(Modifier.height(8.dp))

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

            // Selector de imagen
            Button(
                onClick = { imagePicker.launch("image/*") },
                modifier = Modifier.fillMaxWidth(),
                colors = ButtonDefaults.buttonColors(backgroundColor = DoradoElegante),
                enabled = !isLoading
            ) {
                Text("Seleccionar imagen (opcional)", color = Color.White)
            }
            imageUri?.let {
                Spacer(Modifier.height(8.dp))
                Image(
                    painter = rememberAsyncImagePainter(it),
                    contentDescription = null,
                    modifier = Modifier
                        .size(80.dp)
                        .align(Alignment.CenterHorizontally),
                    contentScale = ContentScale.Crop
                )
            }

            Spacer(Modifier.height(16.dp))

            // Guardar en Firestore
            Button(
                onClick = {
                    isLoading = true
                    // Generar ID y objeto
                    val newId = db.document().id
                    val product = Product(
                        id = newId,
                        name = name,
                        description = description,
                        price = price.toDoubleOrNull() ?: 0.0,
                        category = category,
                        imageUri = imageUri?.toString(),
                        stock = stock.toIntOrNull() ?: 0
                    )
                    // Subir
                    db.document(newId)
                        .set(product)
                        .addOnSuccessListener {
                            isLoading = false
                            showSuccess = true
                            // Limpiar
                            name = ""
                            description = ""
                            price = ""
                            stock = ""
                            category = categories.first()
                            imageUri = null
                        }
                        .addOnFailureListener { e ->
                            isLoading = false
                            showError = "Error guardando: ${e.localizedMessage}"
                        }
                },
                enabled = !isLoading && name.isNotBlank() && description.isNotBlank() && price.isNotBlank(),
                modifier = Modifier.fillMaxWidth(),
                colors = ButtonDefaults.buttonColors(backgroundColor = DoradoElegante)
            ) {
                if (isLoading) CircularProgressIndicator(color = Color.White, strokeWidth = 2.dp)
                else Text("Guardar producto", color = Color.White)
            }
        }
    }
}

@Composable
fun DropdownMenuCategory(
    selected: String,
    options: List<String>,
    onSelect: (String) -> Unit
) {
    var expanded by remember { mutableStateOf(false) }
    Column {
        OutlinedTextField(
            value = selected,
            onValueChange = {},
            label = { Text("Categoría") },
            readOnly = true,
            modifier = Modifier
                .fillMaxWidth()
                .clickable { expanded = true }
        )
        DropdownMenu(
            expanded = expanded,
            onDismissRequest = { expanded = false }
        ) {
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
