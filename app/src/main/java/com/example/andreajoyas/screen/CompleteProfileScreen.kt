package com.example.andreajoyas.screen

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.andreajoyas.R
import com.example.andreajoyas.viewmodel.AuthViewModel
import com.example.andreajoyas.ui.theme.DoradoElegante
import com.example.andreajoyas.ui.theme.FondoSuave

@Composable
fun CompleteProfileScreen(
    authViewModel: AuthViewModel = viewModel(),
    onProfileCompleted: () -> Unit
) {
    val nombre by authViewModel.nombre.collectAsState()
    val apellido by authViewModel.apellido.collectAsState()
    val telefono by authViewModel.telefono.collectAsState()
    val direccion by authViewModel.direccion.collectAsState()

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(FondoSuave)
            .padding(24.dp),
        contentAlignment = Alignment.TopCenter
    ) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            modifier = Modifier
                .fillMaxWidth()
                .padding(top = 40.dp)
        ) {
            Image(
                painter = painterResource(id = R.drawable.logoandrea),
                contentDescription = "Logo de AndreaJoyas",
                modifier = Modifier
                    .height(160.dp)
                    .padding(bottom = 16.dp),
                contentScale = ContentScale.Fit
            )

            Card(
                shape = RoundedCornerShape(20.dp),
                colors = CardDefaults.cardColors(containerColor = Color.White),
                elevation = CardDefaults.cardElevation(10.dp),
                modifier = Modifier.fillMaxWidth()
            ) {
                Column(
                    modifier = Modifier
                        .padding(24.dp)
                        .fillMaxWidth(),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Text("Completar perfil", style = MaterialTheme.typography.titleLarge)

                    Spacer(modifier = Modifier.height(12.dp))

                    OutlinedTextField(
                        value = nombre,
                        onValueChange = { authViewModel.onNombreChanged(it) },
                        label = { Text("Nombre") },
                        modifier = Modifier.fillMaxWidth(),
                        shape = RoundedCornerShape(12.dp),
                        colors = OutlinedTextFieldDefaults.colors(
                            focusedBorderColor = DoradoElegante,
                            unfocusedBorderColor = DoradoElegante.copy(alpha = 0.5f)
                        )
                    )

                    Spacer(modifier = Modifier.height(8.dp))

                    OutlinedTextField(
                        value = apellido,
                        onValueChange = { authViewModel.onApellidoChanged(it) },
                        label = { Text("Apellido") },
                        modifier = Modifier.fillMaxWidth(),
                        shape = RoundedCornerShape(12.dp),
                        colors = OutlinedTextFieldDefaults.colors(
                            focusedBorderColor = DoradoElegante,
                            unfocusedBorderColor = DoradoElegante.copy(alpha = 0.5f)
                        )
                    )

                    Spacer(modifier = Modifier.height(8.dp))

                    OutlinedTextField(
                        value = telefono,
                        onValueChange = { authViewModel.onTelefonoChanged(it) },
                        label = { Text("Teléfono") },
                        modifier = Modifier.fillMaxWidth(),
                        shape = RoundedCornerShape(12.dp),
                        colors = OutlinedTextFieldDefaults.colors(
                            focusedBorderColor = DoradoElegante,
                            unfocusedBorderColor = DoradoElegante.copy(alpha = 0.5f)
                        )
                    )

                    Spacer(modifier = Modifier.height(8.dp))

                    OutlinedTextField(
                        value = direccion,
                        onValueChange = { authViewModel.onDireccionChanged(it) },
                        label = { Text("Dirección") },
                        modifier = Modifier.fillMaxWidth(),
                        shape = RoundedCornerShape(12.dp),
                        colors = OutlinedTextFieldDefaults.colors(
                            focusedBorderColor = DoradoElegante,
                            unfocusedBorderColor = DoradoElegante.copy(alpha = 0.5f)
                        )
                    )

                    Spacer(modifier = Modifier.height(16.dp))

                    Button(
                        onClick = {
                            authViewModel.saveProfile(
                                onSuccess = onProfileCompleted,
                                onError = { /* mostrar error si deseas */ }
                            )
                        },
                        modifier = Modifier.fillMaxWidth(),
                        shape = RoundedCornerShape(12.dp),
                        colors = ButtonDefaults.buttonColors(
                            containerColor = DoradoElegante,
                            contentColor = Color.White
                        )
                    ) {
                        Text("Guardar perfil")
                    }
                }
            }
        }
    }
}
