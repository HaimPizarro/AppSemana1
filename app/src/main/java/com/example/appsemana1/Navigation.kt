package com.example.appsemana1

import androidx.compose.runtime.*
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.appsemana1.auth.AuthViewModel
import com.example.appsemana1.screens.ForgotPasswordScreen
import com.example.appsemana1.screens.HomeScreen
import com.example.appsemana1.screens.LoginScreen
import com.example.appsemana1.screens.RegisterScreen
import com.example.appsemana1.screens.SettingsScreen
import com.example.appsemana1.ui.theme.AccessibilityViewModel

object Routes {
    const val LOGIN = "login"
    const val REGISTER = "register"
    const val FORGOT_PASSWORD = "forgot_password"
    const val HOME = "home"
    const val SETTINGS = "settings"
}

@Composable
fun AppNavigation(
    accessibilityViewModel: AccessibilityViewModel,
    authViewModel: AuthViewModel
) {
    val navController = rememberNavController()
    val currentUser by authViewModel.currentUser
    val start = if (currentUser == null) Routes.LOGIN else Routes.HOME

    NavHost(navController = navController, startDestination = start) {

        composable(Routes.LOGIN) {
            // Lee mensaje de éxito enviado desde Register
            val successMsg = navController.currentBackStackEntry
                ?.savedStateHandle
                ?.get<String>("success_message")

            // Limpia el mensaje para que no reaparezca
            LaunchedEffect(successMsg) {
                if (!successMsg.isNullOrBlank()) {
                    navController.currentBackStackEntry
                        ?.savedStateHandle
                        ?.set("success_message", null)
                }
            }

            LoginScreen(
                onNavigateToRegister = { navController.navigate(Routes.REGISTER) },
                onNavigateToForgotPassword = { navController.navigate(Routes.FORGOT_PASSWORD) },
                onLoginSuccess = {
                    navController.navigate(Routes.HOME) {
                        popUpTo(Routes.LOGIN) { inclusive = true }
                    }
                },
                accessibilityViewModel = accessibilityViewModel,
                authViewModel = authViewModel,
                initialSuccessMessage = successMsg // ⬅️ clave
            )
        }

        composable(Routes.REGISTER) {
            RegisterScreen(
                onNavigateBack = { navController.navigateUp() },
                onRegisterSuccess = {
                    // Escribe el mensaje en el back stack de destino (LOGIN)
                    navController.previousBackStackEntry
                        ?.savedStateHandle
                        ?.set("success_message", "Cuenta creada correctamente")
                    // Vuelve a Login
                    navController.popBackStack()
                },
                authViewModel = authViewModel
            )
        }

        composable(Routes.FORGOT_PASSWORD) {
            ForgotPasswordScreen(
                onNavigateBack = { navController.navigateUp() },
                onNavigateToLogin = {
                    navController.navigate(Routes.LOGIN) {
                        popUpTo(Routes.HOME) { inclusive = false }
                    }
                },
                accessibilityViewModel = accessibilityViewModel,
                authViewModel = authViewModel
            )
        }

        composable(Routes.HOME) {
            HomeScreen(
                onLogout = {
                    authViewModel.logout()
                    navController.navigate(Routes.LOGIN) {
                        popUpTo(Routes.HOME) { inclusive = true }
                    }
                },
                onOpenSettings = { navController.navigate(Routes.SETTINGS) },
                accessibilityViewModel = accessibilityViewModel
            )
        }

        composable(Routes.SETTINGS) {
            SettingsScreen(onNavigateBack = { navController.navigateUp() })
        }
    }
}
