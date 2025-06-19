package com.example.andreajoyas.screen

import android.widget.Toast
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.andreajoyas.viewmodel.AuthViewModel

@Composable
fun ProfileScreen(
    onNavigateBack: () -> Unit,
    authViewModel: AuthViewModel = viewModel()
) {
    val context = LocalContext.current
    val userId = authViewModel.auth.currentUser?.uid

    // Estado local para los campos y el modo de edición
    var localNombre by remember { mutableStateOf("") }
    var localApellido by remember { mutableStateOf("") }
    val localEmail = authViewModel.auth.currentUser?.email ?: "" // Solo lectura
    var localTelefono by remember { mutableStateOf("") }
    var localDireccion by remember { mutableStateOf("") }
    var isLoading by remember { mutableStateOf(true) }
    var isEditable by remember { mutableStateOf(false) }

    // Cargar datos desde Firebase al iniciar
    LaunchedEffect(userId) {
        if (userId != null) {
            authViewModel.db.collection("usuarios").document(userId).get()
                .addOnSuccessListener { document ->
                    val data = document.data
                    localNombre = data?.get("nombre") as? String ?: ""
                    localApellido = data?.get("apellido") as? String ?: ""
                    localTelefono = data?.get("telefono") as? String ?: ""
                    localDireccion = data?.get("direccion") as? String ?: ""
                    isLoading = false
                }
                .addOnFailureListener {
                    Toast.makeText(context, "Error al cargar datos: ${it.message}", Toast.LENGTH_SHORT).show()
                    isLoading = false
                }
        } else {
            Toast.makeText(context, "Usuario no autenticado", Toast.LENGTH_SHORT).show()
            onNavigateBack()
            isLoading = false
        }
    }

    // Mostrar pantalla de carga mientras se obtienen los datos
    if (isLoading) {
        Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
            CircularProgressIndicator()
        }
        return
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
    ) {
        Text(
            text = "Editar Perfil",
            style = MaterialTheme.typography.headlineSmall,
            modifier = Modifier.padding(bottom = 16.dp)
        )

        // Mostrar datos en modo de solo lectura si no está en modo edición
        if (!isEditable) {
            Text("Nombre: $localNombre", modifier = Modifier.padding(bottom = 8.dp))
            Text("Apellido: $localApellido", modifier = Modifier.padding(bottom = 8.dp))
            Text("Correo electrónico: $localEmail", modifier = Modifier.padding(bottom = 8.dp))
            Text("Teléfono: $localTelefono", modifier = Modifier.padding(bottom = 8.dp))
            Text("Dirección: $localDireccion", modifier = Modifier.padding(bottom = 8.dp))

            Button(
                onClick = { isEditable = true },
                modifier = Modifier.fillMaxWidth(),
                colors = ButtonDefaults.buttonColors(containerColor = MaterialTheme.colorScheme.primary)
            ) {
                Text("Editar datos de Perfil")
            }
        } else {
            // Modo edición con textfields
            OutlinedTextField(
                value = localNombre,
                onValueChange = { localNombre = it },
                label = { Text("Nombre") },
                modifier = Modifier.fillMaxWidth()
            )
            Spacer(modifier = Modifier.height(8.dp))

            OutlinedTextField(
                value = localApellido,
                onValueChange = { localApellido = it },
                label = { Text("Apellido") },
                modifier = Modifier.fillMaxWidth()
            )
            Spacer(modifier = Modifier.height(8.dp))

            Text("Correo electrónico: $localEmail", modifier = Modifier.padding(bottom = 8.dp)) // Solo lectura

            OutlinedTextField(
                value = localTelefono,
                onValueChange = { localTelefono = it },
                label = { Text("Teléfono") },
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Phone),
                modifier = Modifier.fillMaxWidth()
            )
            Spacer(modifier = Modifier.height(8.dp))

            OutlinedTextField(
                value = localDireccion,
                onValueChange = { localDireccion = it },
                label = { Text("Dirección") },
                modifier = Modifier.fillMaxWidth()
            )
            Spacer(modifier = Modifier.height(16.dp))

            Button(
                onClick = {
                    if (userId != null) {
                        val data = hashMapOf(
                            "nombre" to localNombre,
                            "apellido" to localApellido,
                            "telefono" to localTelefono,
                            "direccion" to localDireccion
                        )
                        authViewModel.db.collection("usuarios").document(userId)
                            .set(data)
                            .addOnSuccessListener {
                                Toast.makeText(context, "Perfil actualizado correctamente", Toast.LENGTH_SHORT).show()
                                isEditable = false
                            }
                            .addOnFailureListener {
                                Toast.makeText(context, "Error al actualizar: ${it.message}", Toast.LENGTH_SHORT).show()
                            }
                    }
                },
                modifier = Modifier.fillMaxWidth(),
                colors = ButtonDefaults.buttonColors(containerColor = MaterialTheme.colorScheme.primary)
            ) {
                Text("Guardar cambios")
            }

            Spacer(modifier = Modifier.height(8.dp))

            Button(
                onClick = { isEditable = false },
                modifier = Modifier.fillMaxWidth(),
                colors = ButtonDefaults.buttonColors(containerColor = MaterialTheme.colorScheme.error)
            ) {
                Text("Cancelar")
            }
        }
    }
}