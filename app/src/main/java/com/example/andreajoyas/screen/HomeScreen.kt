package com.example.andreajoyas.screen

import androidx.compose.foundation.layout.Column
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment

@Composable
fun HomeScreen(
    onNavigateToProfile: () -> Unit
) {
    Column(
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text("Pantalla Principal - Home")
        Button(
            onClick = onNavigateToProfile
        ) {
            Text("Ir a Perfil")
        }
    }
}