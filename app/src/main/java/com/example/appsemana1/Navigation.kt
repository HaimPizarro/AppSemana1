package com.example.appsemana1

import androidx.compose.runtime.Composable
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.appsemana1.screens.ForgotPasswordScreen
import com.example.appsemana1.screens.HomeScreen
import com.example.appsemana1.screens.LoginScreen
import com.example.appsemana1.screens.RegisterScreen
import com.example.appsemana1.ui.theme.AccessibilityViewModel

object Routes {
    const val LOGIN = "login"
    const val REGISTER = "register"
    const val FORGOT_PASSWORD = "forgot_password"
    const val HOME = "home"
}

@Composable
fun AppNavigation(
    accessibilityViewModel: AccessibilityViewModel
) {
    val navController = rememberNavController()

    NavHost(navController = navController, startDestination = Routes.LOGIN) {

        composable(Routes.LOGIN) {
            LoginScreen(
                onNavigateToRegister = { navController.navigate(Routes.REGISTER) },
                onNavigateToForgotPassword = { navController.navigate(Routes.FORGOT_PASSWORD) },
                onLoginSuccess = {
                    // Limpiar LOGIN del back stack y avanzar a HOME
                    navController.popBackStack(Routes.LOGIN, inclusive = true)
                    navController.navigate(Routes.HOME)
                },
                accessibilityViewModel = accessibilityViewModel
            )
        }

        composable(Routes.REGISTER) {
            RegisterScreen(
                onNavigateBack = { navController.navigateUp() },
                onRegisterSuccess = {
                    // Salir de REGISTER y volver a LOGIN limpio
                    navController.popBackStack(Routes.REGISTER, inclusive = true)
                    navController.navigate(Routes.LOGIN)
                },
                accessibilityViewModel = accessibilityViewModel
            )
        }

        composable(Routes.FORGOT_PASSWORD) {
            ForgotPasswordScreen(
                onNavigateBack = { navController.navigateUp() },
                onNavigateToLogin = {
                    // Salir de FORGOT y volver a LOGIN limpio
                    navController.popBackStack(Routes.FORGOT_PASSWORD, inclusive = true)
                    navController.navigate(Routes.LOGIN)
                },
                accessibilityViewModel = accessibilityViewModel
            )
        }

        composable(Routes.HOME) {
            HomeScreen(
                onLogout = {
                    // Cerrar sesión: limpiar HOME y volver a LOGIN
                    navController.popBackStack(Routes.HOME, inclusive = true)
                    navController.navigate(Routes.LOGIN)
                },
                accessibilityViewModel = accessibilityViewModel
            )
        }
    }
}
