package com.example.appsemana1.screens

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.hapticfeedback.HapticFeedbackType
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalHapticFeedback
import androidx.compose.ui.semantics.contentDescription
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.zIndex
import com.example.appsemana1.ui.theme.AccessibilityViewModel
import com.example.appsemana1.ui.theme.LocalAccessibilitySettings
import com.example.appsemana1.ui.theme.rememberAccessibilityManager
import com.example.appsemana1.auth.AuthViewModel
import com.example.appsemana1.auth.AuthResult

@OptIn(ExperimentalMaterial3Api::class, ExperimentalComposeUiApi::class)
@Composable
fun LoginScreen(
    onNavigateToRegister: () -> Unit,
    onNavigateToForgotPassword: () -> Unit,
    onLoginSuccess: () -> Unit,
    accessibilityViewModel: AccessibilityViewModel,
    authViewModel: AuthViewModel,
    initialSuccessMessage: String? = null // ⬅️ NUEVO
) {
    // Estado local de formulario
    var email by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }
    var passwordVisible by remember { mutableStateOf(false) }
    var showError by remember { mutableStateOf(false) }
    var errorMessage by remember { mutableStateOf("") }

    // Accesibilidad global
    val settings = LocalAccessibilitySettings.current
    val manager = rememberAccessibilityManager()
    val context = LocalContext.current
    var showAccessibilityDialog by remember { mutableStateOf(false) }

    // Helpers
    val haptics = LocalHapticFeedback.current
    val colors = MaterialTheme.colorScheme
    val multiplier = settings.fontSize.multiplier

    // Snackbar
    val snackbarHostState = remember { SnackbarHostState() }
    LaunchedEffect(initialSuccessMessage) {
        val msg = initialSuccessMessage
        if (!msg.isNullOrBlank()) {
            snackbarHostState.showSnackbar(message = msg)
        }
    }

    Scaffold(
        snackbarHost = { SnackbarHost(hostState = snackbarHostState) }
    ) { pv ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(colors.background)
                .padding(pv)
        ) {
            // --- Contenido principal  ---
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .verticalScroll(rememberScrollState())
                    .padding(24.dp)
                    .semantics {
                        contentDescription = "Pantalla de inicio de sesión. Ingrese su email y contraseña para acceder a la aplicación."
                    },
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.Center
            ) {
                // Logo
                Icon(
                    Icons.Default.Person,
                    contentDescription = "Logo de la aplicación",
                    modifier = Modifier
                        .size((100 * multiplier).dp)
                        .padding(bottom = 16.dp),
                    tint = colors.primary
                )

                Text(
                    text = "Bienvenido",
                    fontSize = (32 * multiplier).sp,
                    fontWeight = FontWeight.Bold,
                    color = colors.primary,
                    modifier = Modifier.padding(bottom = 8.dp)
                )

                Text(
                    text = "Inicia sesión para continuar",
                    fontSize = (18 * multiplier).sp,
                    color = colors.onSurface,
                    modifier = Modifier.padding(bottom = 32.dp)
                )

                // Campo Email
                OutlinedTextField(
                    value = email,
                    onValueChange = {
                        email = it
                        if (showError) showError = false
                    },
                    label = { Text("Email", fontSize = (16 * multiplier).sp) },
                    placeholder = { Text("ejemplo@correo.com", fontSize = (14 * multiplier).sp) },
                    leadingIcon = {
                        Icon(
                            Icons.Default.Email,
                            contentDescription = "Ícono email",
                            tint = colors.primary
                        )
                    },
                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Email),
                    singleLine = true,
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(bottom = 16.dp)
                        .semantics { contentDescription = "Campo de email" },
                    shape = RoundedCornerShape(12.dp),
                    isError = showError && (email.isEmpty() || !email.contains("@")),
                    colors = OutlinedTextFieldDefaults.colors(
                        focusedBorderColor = colors.primary,
                        unfocusedBorderColor = colors.outline,
                        errorBorderColor = colors.error,
                        focusedLabelColor = colors.primary,
                        unfocusedLabelColor = colors.onSurface,
                        focusedTextColor = colors.onSurface,
                        unfocusedTextColor = colors.onSurface
                    )
                )

                // Campo Contraseña
                OutlinedTextField(
                    value = password,
                    onValueChange = {
                        password = it
                        if (showError) showError = false
                    },
                    label = { Text("Contraseña", fontSize = (16 * multiplier).sp) },
                    placeholder = { Text("Mínimo 6 caracteres", fontSize = (14 * multiplier).sp) },
                    leadingIcon = {
                        Icon(
                            Icons.Default.Lock,
                            contentDescription = "Ícono contraseña",
                            tint = colors.primary
                        )
                    },
                    trailingIcon = {
                        IconButton(
                            onClick = {
                                passwordVisible = !passwordVisible
                                haptics.performHapticFeedback(HapticFeedbackType.LongPress)
                            }
                        ) {
                            Icon(
                                imageVector = if (passwordVisible) Icons.Default.Visibility else Icons.Default.VisibilityOff,
                                contentDescription = if (passwordVisible) "Ocultar contraseña" else "Mostrar contraseña",
                                tint = colors.primary
                            )
                        }
                    },
                    visualTransformation = if (passwordVisible) VisualTransformation.None else PasswordVisualTransformation(),
                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Password),
                    singleLine = true,
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(bottom = 8.dp)
                        .semantics { contentDescription = "Campo de contraseña" },
                    shape = RoundedCornerShape(12.dp),
                    isError = showError && (password.isEmpty() || password.length < 6),
                    colors = OutlinedTextFieldDefaults.colors(
                        focusedBorderColor = colors.primary,
                        unfocusedBorderColor = colors.outline,
                        errorBorderColor = colors.error,
                        focusedLabelColor = colors.primary,
                        unfocusedLabelColor = colors.onSurface,
                        focusedTextColor = colors.onSurface,
                        unfocusedTextColor = colors.onSurface
                    )
                )

                // Error
                if (showError) {
                    Card(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(top = 8.dp, bottom = 16.dp),
                        colors = CardDefaults.cardColors(containerColor = colors.surface),
                        border = BorderStroke(2.dp, colors.error)
                    ) {
                        Row(
                            modifier = Modifier.padding(12.dp),
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Icon(
                                Icons.Default.Error,
                                contentDescription = "Error",
                                tint = colors.error,
                                modifier = Modifier
                                    .size((20 * multiplier).dp)
                                    .padding(end = 8.dp)
                            )
                            Text(
                                text = errorMessage,
                                color = colors.error,
                                fontSize = (16 * multiplier).sp,
                                fontWeight = FontWeight.Medium
                            )
                        }
                    }
                } else {
                    Spacer(Modifier.height(8.dp))
                }

                // ¿Olvidaste tu contraseña?
                TextButton(
                    onClick = {
                        haptics.performHapticFeedback(HapticFeedbackType.LongPress)
                        onNavigateToForgotPassword()
                    },
                    modifier = Modifier
                        .align(Alignment.End)
                        .padding(bottom = 24.dp)
                ) {
                    Text(
                        "¿Olvidaste tu contraseña?",
                        fontSize = (16 * multiplier).sp,
                        color = colors.primary
                    )
                }

                // Iniciar Sesión
                Button(
                    onClick = {
                        haptics.performHapticFeedback(HapticFeedbackType.LongPress)
                        when {
                            email.isEmpty() -> {
                                showError = true
                                errorMessage = "Por favor ingresa tu email"
                            }
                            !email.contains("@") -> {
                                showError = true
                                errorMessage = "Por favor ingresa un email válido"
                            }
                            password.isEmpty() -> {
                                showError = true
                                errorMessage = "Por favor ingresa tu contraseña"
                            }
                            password.length < 6 -> {
                                showError = true
                                errorMessage = "La contraseña debe tener al menos 6 caracteres"
                            }
                            else -> {
                                when (val res = authViewModel.login(email, password)) {
                                    is AuthResult.Ok -> {
                                        showError = false; errorMessage = ""
                                        onLoginSuccess()
                                    }
                                    is AuthResult.Error -> {
                                        showError = true; errorMessage = res.message
                                    }
                                }
                            }
                        }
                    },
                    enabled = email.isNotEmpty() && password.isNotEmpty(),
                    modifier = Modifier
                        .fillMaxWidth()
                        .height((56 * multiplier).dp),
                    shape = RoundedCornerShape(12.dp),
                    colors = ButtonDefaults.buttonColors(
                        containerColor = colors.primary,
                        contentColor = colors.onPrimary,
                        disabledContainerColor = colors.onSurface.copy(alpha = 0.12f),
                        disabledContentColor = colors.onSurface.copy(alpha = 0.38f)
                    )
                ) {
                    Text(
                        text = "Iniciar Sesión",
                        fontSize = (18 * multiplier).sp,
                        fontWeight = FontWeight.Medium
                    )
                }

                Spacer(modifier = Modifier.height(16.dp))

                // Crear Cuenta
                OutlinedButton(
                    onClick = {
                        haptics.performHapticFeedback(HapticFeedbackType.LongPress)
                        onNavigateToRegister()
                    },
                    modifier = Modifier
                        .fillMaxWidth()
                        .height((56 * multiplier).dp),
                    shape = RoundedCornerShape(12.dp),
                    colors = ButtonDefaults.outlinedButtonColors(
                        containerColor = MaterialTheme.colorScheme.background,
                        contentColor = colors.primary
                    ),
                    border = BorderStroke(2.dp, colors.primary)
                ) {
                    Text(
                        text = "Crear Cuenta",
                        fontSize = (18 * multiplier).sp,
                        fontWeight = FontWeight.Medium
                    )
                }

                Spacer(modifier = Modifier.height(16.dp))
            }

            // --- Botón de Accesibilidad  ---
            manager.AccessibilityIconButton(
                onAccessibilityClick = { showAccessibilityDialog = true },
                modifier = Modifier
                    .align(Alignment.TopEnd)
                    .padding(16.dp)
                    .zIndex(1f)
            )

            // --- Diálogo de Accesibilidad ---
            if (showAccessibilityDialog) {
                manager.AccessibilityDialog(
                    currentSettings = settings,
                    onSettingsChanged = { new ->
                        accessibilityViewModel.updateSettings(context, new)
                    },
                    onDismiss = { showAccessibilityDialog = false }
                )
            }
        }
    }
}

@Preview(name = "Login", showBackground = true, widthDp = 360, heightDp = 720)
@Composable
private fun LoginScreen_Preview() {
    val fakeVm = AccessibilityViewModel()
    val fakeAuth = com.example.appsemana1.auth.AuthViewModel()
    Surface {
        LoginScreen(
            onNavigateToRegister = {},
            onNavigateToForgotPassword = {},
            onLoginSuccess = {},
            accessibilityViewModel = fakeVm,
            authViewModel = fakeAuth,
            initialSuccessMessage = "Cuenta creada correctamente"
        )
    }
}
