package com.example.appsemana1.screens

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
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.semantics.*
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.hapticfeedback.HapticFeedbackType
import androidx.compose.ui.platform.LocalHapticFeedback

// Datos para configuración de accesibilidad
data class AccessibilitySettings(
    val isColorblindMode: Boolean = false,
    val colorblindType: ColorblindType = ColorblindType.NONE,
    val fontSize: FontSize = FontSize.NORMAL,
    val highContrast: Boolean = false
)

enum class ColorblindType {
    NONE, PROTANOPIA, DEUTERANOPIA, TRITANOPIA, MONOCHROMATIC
}

enum class FontSize(val multiplier: Float) {
    SMALL(0.9f), NORMAL(1.0f), LARGE(1.2f), EXTRA_LARGE(1.5f)
}

// Esquema de colores para daltonismo
@Composable
fun getAccessibilityColors(settings: AccessibilitySettings): AccessibilityColorScheme {
    return when {
        settings.highContrast -> AccessibilityColorScheme(
            primary = Color(0xFF000000),
            onPrimary = Color(0xFFFFFFFF),
            surface = Color(0xFFFFFFFF),
            onSurface = Color(0xFF000000),
            error = Color(0xFF000000),
            onError = Color(0xFFFFFFFF),
            outline = Color(0xFF000000),
            background = Color(0xFFFFFFFF)
        )
        settings.colorblindType == ColorblindType.PROTANOPIA -> AccessibilityColorScheme(
            primary = Color(0xFF0066CC),
            onPrimary = Color(0xFFFFFFFF),
            surface = Color(0xFFF8F9FA),
            onSurface = Color(0xFF1A1A1A),
            error = Color(0xFF0066CC),
            onError = Color(0xFFFFFFFF),
            outline = Color(0xFF666666),
            background = Color(0xFFFFFFFF)
        )
        settings.colorblindType == ColorblindType.DEUTERANOPIA -> AccessibilityColorScheme(
            primary = Color(0xFF6600CC),
            onPrimary = Color(0xFFFFFFFF),
            surface = Color(0xFFF8F9FA),
            onSurface = Color(0xFF1A1A1A),
            error = Color(0xFF6600CC),
            onError = Color(0xFFFFFFFF),
            outline = Color(0xFF666666),
            background = Color(0xFFFFFFFF)
        )
        settings.colorblindType == ColorblindType.TRITANOPIA -> AccessibilityColorScheme(
            primary = Color(0xFFCC6600),
            onPrimary = Color(0xFFFFFFFF),
            surface = Color(0xFFF8F9FA),
            onSurface = Color(0xFF1A1A1A),
            error = Color(0xFFCC0066),
            onError = Color(0xFFFFFFFF),
            outline = Color(0xFF666666),
            background = Color(0xFFFFFFFF)
        )
        settings.colorblindType == ColorblindType.MONOCHROMATIC -> AccessibilityColorScheme(
            primary = Color(0xFF333333),
            onPrimary = Color(0xFFFFFFFF),
            surface = Color(0xFFF0F0F0),
            onSurface = Color(0xFF000000),
            error = Color(0xFF000000),
            onError = Color(0xFFFFFFFF),
            outline = Color(0xFF666666),
            background = Color(0xFFFFFFFF)
        )
        else -> AccessibilityColorScheme(
            primary = Color(0xFF6750A4),
            onPrimary = Color(0xFFFFFFFF),
            surface = Color(0xFFFEF7FF),
            onSurface = Color(0xFF1D1B20),
            error = Color(0xFFBA1A1A),
            onError = Color(0xFFFFFFFF),
            outline = Color(0xFF79747E),
            background = Color(0xFFFFFBFE)
        )
    }
}

data class AccessibilityColorScheme(
    val primary: Color,
    val onPrimary: Color,
    val surface: Color,
    val onSurface: Color,
    val error: Color,
    val onError: Color,
    val outline: Color,
    val background: Color
)

@OptIn(ExperimentalMaterial3Api::class, ExperimentalComposeUiApi::class)
@Composable
fun LoginScreen(
    onNavigateToRegister: () -> Unit,
    onNavigateToForgotPassword: () -> Unit,
    onLoginSuccess: () -> Unit
) {
    var email by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }
    var passwordVisible by remember { mutableStateOf(false) }
    var showError by remember { mutableStateOf(false) }
    var errorMessage by remember { mutableStateOf("") }
    var showAccessibilityDialog by remember { mutableStateOf(false) }
    var accessibilitySettings by remember { mutableStateOf(AccessibilitySettings()) }

    val hapticFeedback = LocalHapticFeedback.current
    val colors = getAccessibilityColors(accessibilitySettings)

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(colors.background)
    ) {
        // Botón de Accesibilidad en la esquina superior derecha
        IconButton(
            onClick = {
                hapticFeedback.performHapticFeedback(HapticFeedbackType.LongPress)
                showAccessibilityDialog = true
            },
            modifier = Modifier
                .align(Alignment.TopEnd)
                .padding(16.dp)
                .semantics {
                    contentDescription = "Botón de configuración de accesibilidad. Presiona para abrir opciones de accesibilidad"
                }
        ) {
            Icon(
                Icons.Default.Accessibility,
                contentDescription = "Configuración de accesibilidad",
                tint = colors.primary,
                modifier = Modifier.size(28.dp)
            )
        }

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
                    .size((100 * accessibilitySettings.fontSize.multiplier).dp)
                    .padding(bottom = 16.dp)
                    .semantics {
                        contentDescription = "Logo de la aplicación de reservas"
                    },
                tint = colors.primary
            )

            Text(
                text = "Bienvenido",
                fontSize = (32 * accessibilitySettings.fontSize.multiplier).sp,
                fontWeight = FontWeight.Bold,
                color = colors.primary,
                modifier = Modifier
                    .padding(bottom = 8.dp)
                    .semantics {
                        contentDescription = "Título: Bienvenido"
                    }
            )

            Text(
                text = "Inicia sesión para continuar",
                fontSize = (18 * accessibilitySettings.fontSize.multiplier).sp,
                color = colors.onSurface,
                modifier = Modifier
                    .padding(bottom = 32.dp)
                    .semantics {
                        contentDescription = "Subtítulo: Inicia sesión para continuar"
                    }
            )

            // Campo de Email
            OutlinedTextField(
                value = email,
                onValueChange = {
                    email = it
                    showError = false
                },
                label = {
                    Text(
                        "Email",
                        fontSize = (16 * accessibilitySettings.fontSize.multiplier).sp,
                        modifier = Modifier.semantics {
                            contentDescription = "Campo de email requerido"
                        }
                    )
                },
                placeholder = {
                    Text(
                        "ejemplo@correo.com",
                        fontSize = (14 * accessibilitySettings.fontSize.multiplier).sp,
                        modifier = Modifier.semantics {
                            contentDescription = "Ejemplo: ejemplo arroba correo punto com"
                        }
                    )
                },
                leadingIcon = {
                    Icon(
                        Icons.Default.Email,
                        contentDescription = "Ícono de email",
                        tint = colors.primary,
                        modifier = Modifier.semantics {
                            contentDescription = "Ícono indicador de campo de correo electrónico"
                        }
                    )
                },
                keyboardOptions = KeyboardOptions(
                    keyboardType = KeyboardType.Email
                ),
                singleLine = true,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(bottom = 16.dp)
                    .semantics {
                        contentDescription = if (showError && email.isEmpty()) {
                            "Campo de email obligatorio. Error: $errorMessage"
                        } else if (showError && !email.contains("@")) {
                            "Campo de email. Error: formato de email inválido"
                        } else {
                            "Campo de email. Ingrese su dirección de correo electrónico"
                        }
                    },
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

            // Campo de Contraseña
            OutlinedTextField(
                value = password,
                onValueChange = {
                    password = it
                    showError = false
                },
                label = {
                    Text(
                        "Contraseña",
                        fontSize = (16 * accessibilitySettings.fontSize.multiplier).sp,
                        modifier = Modifier.semantics {
                            contentDescription = "Campo de contraseña requerido"
                        }
                    )
                },
                placeholder = {
                    Text(
                        "Mínimo 6 caracteres",
                        fontSize = (14 * accessibilitySettings.fontSize.multiplier).sp,
                        modifier = Modifier.semantics {
                            contentDescription = "La contraseña debe tener mínimo 6 caracteres"
                        }
                    )
                },
                leadingIcon = {
                    Icon(
                        Icons.Default.Lock,
                        contentDescription = "Ícono de contraseña",
                        tint = colors.primary,
                        modifier = Modifier.semantics {
                            contentDescription = "Ícono indicador de campo de contraseña"
                        }
                    )
                },
                trailingIcon = {
                    IconButton(
                        onClick = {
                            passwordVisible = !passwordVisible
                            hapticFeedback.performHapticFeedback(HapticFeedbackType.LongPress)
                        },
                        modifier = Modifier.semantics {
                            contentDescription = if (passwordVisible) {
                                "Botón para ocultar contraseña. La contraseña está actualmente visible"
                            } else {
                                "Botón para mostrar contraseña. La contraseña está actualmente oculta"
                            }
                        }
                    ) {
                        Icon(
                            imageVector = if (passwordVisible)
                                Icons.Default.Visibility
                            else
                                Icons.Default.VisibilityOff,
                            contentDescription = if (passwordVisible)
                                "Ocultar contraseña"
                            else
                                "Mostrar contraseña",
                            tint = colors.primary
                        )
                    }
                },
                visualTransformation = if (passwordVisible)
                    VisualTransformation.None
                else
                    PasswordVisualTransformation(),
                keyboardOptions = KeyboardOptions(
                    keyboardType = KeyboardType.Password
                ),
                singleLine = true,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(bottom = 8.dp)
                    .semantics {
                        contentDescription = if (showError && password.isEmpty()) {
                            "Campo de contraseña obligatorio. Error: $errorMessage"
                        } else if (showError && password.length < 6) {
                            "Campo de contraseña. Error: debe tener al menos 6 caracteres"
                        } else {
                            "Campo de contraseña. ${if (passwordVisible) "Texto visible" else "Texto oculto por seguridad"}"
                        }
                    },
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

            // Mensaje de error
            if (showError) {
                Card(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(bottom = 16.dp)
                        .semantics {
                            contentDescription = "Error: $errorMessage"
                        },
                    colors = CardDefaults.cardColors(
                        containerColor = colors.surface
                    ),
                    border = androidx.compose.foundation.BorderStroke(
                        2.dp,
                        colors.error
                    )
                ) {
                    Row(
                        modifier = Modifier.padding(12.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Icon(
                            Icons.Default.Error,
                            contentDescription = "Ícono de error",
                            tint = colors.error,
                            modifier = Modifier
                                .size((20 * accessibilitySettings.fontSize.multiplier).dp)
                                .padding(end = 8.dp)
                        )
                        Text(
                            text = errorMessage,
                            color = colors.error,
                            fontSize = (16 * accessibilitySettings.fontSize.multiplier).sp,
                            fontWeight = FontWeight.Medium
                        )
                    }
                }
            }

            // Botón de olvidar contraseña
            TextButton(
                onClick = {
                    hapticFeedback.performHapticFeedback(HapticFeedbackType.LongPress)
                    onNavigateToForgotPassword()
                },
                modifier = Modifier
                    .align(Alignment.End)
                    .padding(bottom = 24.dp)
                    .semantics {
                        contentDescription = "Botón: ¿Olvidaste tu contraseña? Presiona para recuperar tu contraseña"
                    }
            ) {
                Text(
                    "¿Olvidaste tu contraseña?",
                    fontSize = (16 * accessibilitySettings.fontSize.multiplier).sp,
                    color = colors.primary
                )
            }

            // Botón de Iniciar Sesión
            Button(
                onClick = {
                    hapticFeedback.performHapticFeedback(HapticFeedbackType.LongPress)
                    when {
                        email.isEmpty() -> {
                            showError = true
                            errorMessage = "Por favor ingresa tu email"
                        }
                        password.isEmpty() -> {
                            showError = true
                            errorMessage = "Por favor ingresa tu contraseña"
                        }
                        !email.contains("@") -> {
                            showError = true
                            errorMessage = "Por favor ingresa un email válido"
                        }
                        password.length < 6 -> {
                            showError = true
                            errorMessage = "La contraseña debe tener al menos 6 caracteres"
                        }
                        else -> {
                            showError = false
                            errorMessage = ""
                            onLoginSuccess()
                        }
                    }
                },
                modifier = Modifier
                    .fillMaxWidth()
                    .height((56 * accessibilitySettings.fontSize.multiplier).dp)
                    .semantics {
                        contentDescription = if (email.isNotEmpty() && password.isNotEmpty()) {
                            "Botón: Iniciar Sesión. Presiona para acceder a tu cuenta"
                        } else {
                            "Botón: Iniciar Sesión deshabilitado. Complete los campos de email y contraseña"
                        }
                    },
                shape = RoundedCornerShape(12.dp),
                enabled = email.isNotEmpty() && password.isNotEmpty(),
                colors = ButtonDefaults.buttonColors(
                    containerColor = colors.primary,
                    contentColor = colors.onPrimary,
                    disabledContainerColor = colors.onSurface.copy(alpha = 0.12f),
                    disabledContentColor = colors.onSurface.copy(alpha = 0.38f)
                )
            ) {
                Text(
                    text = "Iniciar Sesión",
                    fontSize = (18 * accessibilitySettings.fontSize.multiplier).sp,
                    fontWeight = FontWeight.Medium
                )
            }

            Spacer(modifier = Modifier.height(16.dp))

            // Botón de Crear Cuenta
            OutlinedButton(
                onClick = {
                    hapticFeedback.performHapticFeedback(HapticFeedbackType.LongPress)
                    onNavigateToRegister()
                },
                modifier = Modifier
                    .fillMaxWidth()
                    .height((56 * accessibilitySettings.fontSize.multiplier).dp)
                    .semantics {
                        contentDescription = "Botón: Crear Cuenta. Presiona para registrarte como nuevo usuario"
                    },
                shape = RoundedCornerShape(12.dp),
                colors = ButtonDefaults.outlinedButtonColors(
                    containerColor = Color.Transparent,
                    contentColor = colors.primary
                ),
                border = androidx.compose.foundation.BorderStroke(
                    2.dp,
                    colors.primary
                )
            ) {
                Text(
                    text = "Crear Cuenta",
                    fontSize = (18 * accessibilitySettings.fontSize.multiplier).sp,
                    fontWeight = FontWeight.Medium
                )
            }

            Spacer(modifier = Modifier.height(16.dp))
        }

        // Diálogo de Accesibilidad
        if (showAccessibilityDialog) {
            AccessibilityDialog(
                currentSettings = accessibilitySettings,
                onSettingsChanged = { accessibilitySettings = it },
                onDismiss = { showAccessibilityDialog = false }
            )
        }
    }
}

@Composable
fun AccessibilityDialog(
    currentSettings: AccessibilitySettings,
    onSettingsChanged: (AccessibilitySettings) -> Unit,
    onDismiss: () -> Unit
) {
    val hapticFeedback = LocalHapticFeedback.current

    AlertDialog(
        onDismissRequest = onDismiss,
        title = {
            Text(
                "Configuración de Accesibilidad",
                fontWeight = FontWeight.Bold,
                fontSize = 20.sp
            )
        },
        text = {
            Column(
                modifier = Modifier.verticalScroll(rememberScrollState())
            ) {
                Text(
                    "Personaliza la aplicación según tus necesidades:",
                    fontSize = 16.sp,
                    modifier = Modifier.padding(bottom = 16.dp)
                )

                // Pregunta sobre daltonismo
                Text(
                    "¿Tienes dificultades para distinguir colores?",
                    fontWeight = FontWeight.Medium,
                    fontSize = 16.sp,
                    modifier = Modifier.padding(bottom = 8.dp)
                )

                // Opciones de daltonismo
                ColorblindType.values().forEach { type ->
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(vertical = 4.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        RadioButton(
                            selected = currentSettings.colorblindType == type,
                            onClick = {
                                hapticFeedback.performHapticFeedback(HapticFeedbackType.LongPress)
                                onSettingsChanged(
                                    currentSettings.copy(
                                        colorblindType = type,
                                        isColorblindMode = type != ColorblindType.NONE
                                    )
                                )
                            }
                        )
                        Text(
                            text = when (type) {
                                ColorblindType.NONE -> "Sin dificultades"
                                ColorblindType.PROTANOPIA -> "Dificultad con rojos"
                                ColorblindType.DEUTERANOPIA -> "Dificultad con verdes"
                                ColorblindType.TRITANOPIA -> "Dificultad con azules"
                                ColorblindType.MONOCHROMATIC -> "Solo veo en escala de grises"
                            },
                            fontSize = 14.sp,
                            modifier = Modifier.padding(start = 8.dp)
                        )
                    }
                }

                Spacer(modifier = Modifier.height(16.dp))

                // Tamaño de texto
                Text(
                    "Tamaño del texto:",
                    fontWeight = FontWeight.Medium,
                    fontSize = 16.sp,
                    modifier = Modifier.padding(bottom = 8.dp)
                )

                FontSize.values().forEach { size ->
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(vertical = 4.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        RadioButton(
                            selected = currentSettings.fontSize == size,
                            onClick = {
                                hapticFeedback.performHapticFeedback(HapticFeedbackType.LongPress)
                                onSettingsChanged(currentSettings.copy(fontSize = size))
                            }
                        )
                        Text(
                            text = when (size) {
                                FontSize.SMALL -> "Pequeño"
                                FontSize.NORMAL -> "Normal"
                                FontSize.LARGE -> "Grande"
                                FontSize.EXTRA_LARGE -> "Extra Grande"
                            },
                            fontSize = (14 * size.multiplier).sp,
                            modifier = Modifier.padding(start = 8.dp)
                        )
                    }
                }

                Spacer(modifier = Modifier.height(16.dp))

                // Alto contraste
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Checkbox(
                        checked = currentSettings.highContrast,
                        onCheckedChange = {
                            hapticFeedback.performHapticFeedback(HapticFeedbackType.LongPress)
                            onSettingsChanged(currentSettings.copy(highContrast = it))
                        }
                    )
                    Text(
                        "Modo alto contraste (máxima visibilidad)",
                        fontSize = 14.sp,
                        modifier = Modifier.padding(start = 8.dp)
                    )
                }
            }
        },
        confirmButton = {
            TextButton(
                onClick = {
                    hapticFeedback.performHapticFeedback(HapticFeedbackType.LongPress)
                    onDismiss()
                }
            ) {
                Text("Aplicar", fontSize = 16.sp, fontWeight = FontWeight.Medium)
            }
        },
        dismissButton = {
            TextButton(
                onClick = {
                    hapticFeedback.performHapticFeedback(HapticFeedbackType.LongPress)
                    onSettingsChanged(AccessibilitySettings()) // Resetear
                    onDismiss()
                }
            ) {
                Text("Restablecer", fontSize = 16.sp)
            }
        }
    )
}

@Preview(name = "Login", showBackground = true, widthDp = 360, heightDp = 720)
@Composable
fun LoginScreen_Preview() {
    MaterialTheme {
        LoginScreen(
            onNavigateToRegister = {},
            onNavigateToForgotPassword = {},
            onLoginSuccess = {}
        )
    }
}