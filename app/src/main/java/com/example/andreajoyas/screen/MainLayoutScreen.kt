package com.example.andreajoyas.screen

import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Menu
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import com.example.andreajoyas.viewmodel.CarritoViewModel
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MainLayoutScreen(
    authViewModel: AuthViewModel,
    carritoViewModel: CarritoViewModel,
    onLogout: () -> Unit
) {
    val drawerState = rememberDrawerState(DrawerValue.Closed)
    val scope = rememberCoroutineScope()
    val innerNavController = rememberNavController()

    val currentRoute = innerNavController.currentBackStackEntryAsState().value?.destination?.route

    ModalNavigationDrawer(
        drawerState = drawerState,
        drawerContent = {
            ModalDrawerSheet(
                drawerContainerColor = Color.White,
                drawerTonalElevation = 8.dp
            ) {
                Text("Menú", style = MaterialTheme.typography.titleMedium, modifier = Modifier.padding(16.dp))

                NavigationDrawerItem(
                    label = { Text("Ver Catálogo") },
                    selected = currentRoute == "catalogo",
                    onClick = {
                        innerNavController.navigate("catalogo") {
                            popUpTo(innerNavController.graph.startDestinationId) { saveState = true }
                            launchSingleTop = true
                            restoreState = true
                        }
                        scope.launch { drawerState.close() }
                    }
                )

                NavigationDrawerItem(
                    label = { Text("Mi Perfil") },
                    selected = currentRoute == "perfil",
                    onClick = {
                        innerNavController.navigate("perfil") {
                            popUpTo(innerNavController.graph.startDestinationId) { saveState = true }
                            launchSingleTop = true
                            restoreState = true
                        }
                        scope.launch { drawerState.close() }
                    }
                )

                NavigationDrawerItem(
                    label = { Text("Ver Carrito") },
                    selected = currentRoute == "carrito",
                    onClick = {
                        innerNavController.navigate("carrito") {
                            popUpTo(innerNavController.graph.startDestinationId) { saveState = true }
                            launchSingleTop = true
                            restoreState = true
                        }
                        scope.launch { drawerState.close() }
                    }
                )

                NavigationDrawerItem(
                    label = { Text("Cerrar sesión", color = Color.Red) },
                    selected = false,
                    onClick = {
                        scope.launch { drawerState.close() }
                        onLogout()
                    }
                )
            }
        }
    ) {
        Scaffold(
            topBar = {
                CenterAlignedTopAppBar(
                    title = { Text("AndreaJoyas") },
                    navigationIcon = {
                        IconButton(onClick = {
                            scope.launch { drawerState.open() }
                        }) {
                            Icon(Icons.Default.Menu, contentDescription = "Menú")
                        }
                    }
                )
            }
        ) { innerPadding ->
            NavHost(
                navController = innerNavController,
                startDestination = "catalogo",
                modifier = Modifier.padding(innerPadding)
            ) {
                composable("catalogo") {
                    CatalogoScreen(navController = innerNavController)
                }
                composable("perfil") {
                    PerfilScreen(authViewModel = authViewModel, onBack = { })
                }
                composable("carrito") {
                    CarritoScreen(
                        carritoViewModel = carritoViewModel,
                        onBack = { innerNavController.popBackStack() },
                        onPagar = { innerNavController.navigate("checkout") }
                    )
                }

                composable("detalle/{productoId}") { backStackEntry ->
                    val productoId = backStackEntry.arguments?.getString("productoId") ?: ""
                    DetalleProductoScreen(
                        productoId = productoId,
                        onBack = { innerNavController.popBackStack() },
                        carritoViewModel = carritoViewModel
                    )
                }
                composable("checkout") {
                    CheckoutScreen(
                        carritoViewModel = carritoViewModel,
                        onConfirmarPago = {
                            innerNavController.navigate("pago_exitoso")
                        },
                        onCancelar = { innerNavController.popBackStack() }
                    )
                }
                composable("pago_exitoso") {
                    PagoExitosoScreen(onFinalizar = {
                        innerNavController.navigate("catalogo") {
                            popUpTo("catalogo") { inclusive = true }
                        }
                    })
                }
            }
        }
    }
}
