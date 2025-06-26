package com.example.andreajoyas.navigation

import androidx.compose.runtime.Composable
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import com.example.andreajoyas.screen.*
import com.example.andreajoyas.viewmodel.AuthViewModel

@Composable
fun AppNavGraph(navController: NavHostController) {
    val authViewModel: AuthViewModel = viewModel()

    NavHost(navController = navController, startDestination = "login") {
        composable("login") {
            LoginScreen(
                onNavigateToRegister        = { navController.navigate("register") },
                onLoginSuccess              = { userRole ->
                    if (userRole == "admin") {
                        navController.navigate("admin")
                    } else if (userRole == "cliente") {
                        authViewModel.isProfileComplete { isComplete ->
                            if (isComplete) {
                                navController.navigate("home")
                            } else {
                                navController.navigate("complete_profile")
                            }
                        }
                    } else {
                        navController.navigate("login")
                    }
                },
                onNavigateToCompleteProfile = { navController.navigate("complete_profile") },
                authViewModel               = authViewModel
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
                authViewModel      = authViewModel,
                onProfileCompleted = { navController.navigate("home") }
            )
        }

        composable("home") {
            HomeScreen(
                authViewModel = authViewModel,
                onLogout      = {
                    navController.navigate("login") {
                        popUpTo("login") { inclusive = true }
                    }
                }
            )
        }

        composable("admin") {
            AdminScreen(
                authViewModel = authViewModel,
                onLogout      = {
                    navController.navigate("login") {
                        popUpTo(0) { inclusive = true }
                    }
                },
                navController = navController
            )
        }

        composable("add_product") {
            AddProductScreen(
                navController   = navController,
                onProductAdded  = { navController.popBackStack() }
            )
        }
    }
}
