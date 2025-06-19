package com.example.andreajoyas.navigation

import androidx.compose.runtime.Composable
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.andreajoyas.screen.CompleteProfileScreen
import com.example.andreajoyas.screen.HomeScreen
import com.example.andreajoyas.screen.LoginScreen
import com.example.andreajoyas.screen.ProfileScreen
import com.example.andreajoyas.screen.RegisterScreen
import com.example.andreajoyas.viewmodel.AuthViewModel
import androidx.lifecycle.viewmodel.compose.viewModel

@Composable
fun AppNavGraph() {
    val navController = rememberNavController()
    val authViewModel: AuthViewModel = viewModel()

    NavHost(navController = navController, startDestination = "login") {
        composable("login") {
            LoginScreen(
                onNavigateToRegister = { navController.navigate("register") },
                onLoginSuccess = { navController.navigate("home") },
                onNavigateToCompleteProfile = { navController.navigate("complete_profile") },
                authViewModel = authViewModel
            )
        }
        composable("register") {
            RegisterScreen(
                onRegisterSuccess = { navController.navigate("login") },
                onNavigateToLogin = { navController.navigate("login") }
            )
        }
        composable("complete_profile") {
            CompleteProfileScreen(
                authViewModel = authViewModel,
                onProfileCompleted = { navController.navigate("home") }
            )
        }
        composable("home") {
            HomeScreen(
                onNavigateToProfile = { navController.navigate("profile") },
            )
        }
        composable("profile") {
            ProfileScreen(
                onNavigateBack = { navController.popBackStack() }
            )
        }
    }
}
