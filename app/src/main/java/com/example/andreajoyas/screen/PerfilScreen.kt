package com.example.andreajoyas.screen

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.andreajoyas.ui.theme.DoradoElegante
import com.example.andreajoyas.ui.theme.FondoSuave
import com.example.andreajoyas.viewmodel.AuthViewModel

@Composable
fun PerfilScreen(
    authViewModel: AuthViewModel = viewModel(),
    onBack: () -> Unit
) {
    val nombre by authViewModel.nombre.collectAsState()
    val apellido by authViewModel.apellido.collectAsState()
    val telefono by authViewModel.telefono.collectAsState()
    val direccion by authViewModel.direccion.collectAsState()
    var showSaveConfirmation by remember { mutableStateOf(false) }

    // Cargar datos existentes desde Firestore (solo si aún no están cargados)
    LaunchedEffect(Unit) {
        authViewModel.loadUserData()
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(FondoSuave)
            .padding(24.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text("Mi Perfil", style = MaterialTheme.typography.headlineSmall)

        Spacer(modifier = Modifier.height(16.dp))

        OutlinedTextField(
            value = nombre,
            onValueChange = { authViewModel.onNombreChanged(it) },
            label = { Text("Nombre") },
            modifier = Modifier.fillMaxWidth(),
            shape = RoundedCornerShape(12.dp)
        )

        Spacer(modifier = Modifier.height(12.dp))

        OutlinedTextField(
            value = apellido,
            onValueChange = { authViewModel.onApellidoChanged(it) },
            label = { Text("Apellido") },
            modifier = Modifier.fillMaxWidth(),
            shape = RoundedCornerShape(12.dp)
        )

        Spacer(modifier = Modifier.height(12.dp))

        OutlinedTextField(
            value = telefono,
            onValueChange = { authViewModel.onTelefonoChanged(it) },
            label = { Text("Teléfono") },
            modifier = Modifier.fillMaxWidth(),
            shape = RoundedCornerShape(12.dp)
        )

        Spacer(modifier = Modifier.height(12.dp))

        OutlinedTextField(
            value = direccion,
            onValueChange = { authViewModel.onDireccionChanged(it) },
            label = { Text("Dirección") },
            modifier = Modifier.fillMaxWidth(),
            shape = RoundedCornerShape(12.dp)
        )

        Spacer(modifier = Modifier.height(24.dp))

        Button(
            onClick = {
                authViewModel.saveProfile(
                    onSuccess = { showSaveConfirmation = true },
                    onError = { /* puedes manejar errores aquí */ }
                )
            },
            modifier = Modifier.fillMaxWidth(),
            colors = ButtonDefaults.buttonColors(containerColor = DoradoElegante)
        ) {
            Text("Guardar cambios", color = Color.White)
        }

        if (showSaveConfirmation) {
            Text(
                text = "Perfil actualizado correctamente.",
                color = Color.Green,
                modifier = Modifier.padding(top = 12.dp)
            )
        }

        Spacer(modifier = Modifier.height(16.dp))

        Button(
            onClick = onBack,
            modifier = Modifier.fillMaxWidth(),
            colors = ButtonDefaults.buttonColors(containerColor = Color.LightGray)
        ) {
            Text("Volver")
        }
    }
}
