package com.example.appsemana1

import androidx.compose.runtime.Composable
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
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
    accessibilityViewModel: AccessibilityViewModel
) {
    val navController = rememberNavController()

    NavHost(navController = navController, startDestination = Routes.LOGIN) {

        composable(Routes.LOGIN) {
            LoginScreen(
                onNavigateToRegister = { navController.navigate(Routes.REGISTER) },
                onNavigateToForgotPassword = { navController.navigate(Routes.FORGOT_PASSWORD) },
                onLoginSuccess = {
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
                    navController.popBackStack(Routes.FORGOT_PASSWORD, inclusive = true)
                    navController.navigate(Routes.LOGIN)
                },
                accessibilityViewModel = accessibilityViewModel
            )
        }

        composable(Routes.HOME) {
            HomeScreen(
                onLogout = {
                    navController.popBackStack(Routes.HOME, inclusive = true)
                    navController.navigate(Routes.LOGIN)
                },
                onOpenSettings = {
                    navController.navigate(Routes.SETTINGS)
                },
                accessibilityViewModel = accessibilityViewModel
            )
        }

        composable(Routes.SETTINGS) {
            SettingsScreen(
                onNavigateBack = { navController.navigateUp() }
            )
        }
    }
}