package com.example.appsemana1.screens

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.Login
import androidx.compose.material.icons.automirrored.filled.Send
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

@Composable
fun ForgotPasswordScreen(
    onNavigateBack: () -> Unit,
    onNavigateToLogin: () -> Unit,
    accessibilityViewModel: com.example.appsemana1.ui.theme.AccessibilityViewModel
) {
    var email by remember { mutableStateOf("") }
    var showError by remember { mutableStateOf(false) }
    var showSuccess by remember { mutableStateOf(false) }
    var isLoading by remember { mutableStateOf(false) }
    var message by remember { mutableStateOf("") }
    var emailSent by remember { mutableStateOf(false) }
    val coroutineScope = rememberCoroutineScope()

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background)
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .verticalScroll(rememberScrollState())
                .padding(24.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            // Botón de volver
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically
            ) {
                IconButton(
                    onClick = onNavigateBack
                ) {
                    Icon(
                        Icons.Default.ArrowBack,
                        contentDescription = "Volver",
                        tint = MaterialTheme.colorScheme.primary
                    )
                }
                Text(
                    text = "Volver",
                    color = MaterialTheme.colorScheme.primary,
                    modifier = Modifier.clickable { onNavigateBack() }
                )
            }

            Spacer(modifier = Modifier.height(48.dp))

            // Icono animado
            AnimatedVisibility(
                visible = !emailSent,
                enter = fadeIn(),
                exit = fadeOut()
            ) {
                Icon(
                    Icons.Default.Lock,
                    contentDescription = "Recuperar Contraseña",
                    modifier = Modifier
                        .size(100.dp)
                        .padding(bottom = 24.dp),
                    tint = MaterialTheme.colorScheme.primary
                )
            }

            AnimatedVisibility(
                visible = emailSent,
                enter = fadeIn(),
                exit = fadeOut()
            ) {
                Icon(
                    Icons.Default.MarkEmailRead,
                    contentDescription = "Email Enviado",
                    modifier = Modifier
                        .size(100.dp)
                        .padding(bottom = 24.dp),
                    tint = MaterialTheme.colorScheme.primary
                )
            }

            // Título
            Text(
                text = if (emailSent) "¡Correo Enviado!" else "Recuperar Contraseña",
                fontSize = 28.sp,
                fontWeight = FontWeight.Bold,
                color = MaterialTheme.colorScheme.primary,
                modifier = Modifier.padding(bottom = 16.dp),
                textAlign = TextAlign.Center
            )

            // Descripción
            Text(
                text = if (emailSent) {
                    "Hemos enviado las instrucciones para restablecer tu contraseña a:\n$email"
                } else {
                    "Ingresa tu email y te enviaremos instrucciones para restablecer tu contraseña"
                },
                fontSize = 14.sp,
                color = MaterialTheme.colorScheme.onSurfaceVariant,
                textAlign = TextAlign.Center,
                modifier = Modifier.padding(bottom = 32.dp)
            )

            if (!emailSent) {
                // Campo de Email
                OutlinedTextField(
                    value = email,
                    onValueChange = {
                        email = it
                        showError = false
                        showSuccess = false
                    },
                    label = { Text("Email") },
                    leadingIcon = {
                        Icon(Icons.Default.Email, contentDescription = "Email")
                    },
                    trailingIcon = {
                        if (email.isNotEmpty()) {
                            IconButton(onClick = { email = "" }) {
                                Icon(
                                    Icons.Default.Clear,
                                    contentDescription = "Limpiar"
                                )
                            }
                        }
                    },
                    keyboardOptions = KeyboardOptions(
                        keyboardType = KeyboardType.Email
                    ),
                    singleLine = true,
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(bottom = 16.dp),
                    shape = RoundedCornerShape(12.dp),
                    isError = showError,
                    enabled = !isLoading
                )

                // Mensaje de error o éxito
                AnimatedVisibility(
                    visible = showError || showSuccess,
                    enter = fadeIn(),
                    exit = fadeOut()
                ) {
                    Card(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(bottom = 16.dp),
                        colors = CardDefaults.cardColors(
                            containerColor = if (showSuccess)
                                MaterialTheme.colorScheme.primaryContainer
                            else
                                MaterialTheme.colorScheme.errorContainer
                        )
                    ) {
                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(12.dp),
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Icon(
                                imageVector = if (showSuccess)
                                    Icons.Default.CheckCircle
                                else
                                    Icons.Default.Error,
                                contentDescription = null,
                                tint = if (showSuccess)
                                    MaterialTheme.colorScheme.primary
                                else
                                    MaterialTheme.colorScheme.error
                            )
                            Spacer(modifier = Modifier.width(8.dp))
                            Text(
                                text = message,
                                fontSize = 14.sp,
                                color = if (showSuccess)
                                    MaterialTheme.colorScheme.onPrimaryContainer
                                else
                                    MaterialTheme.colorScheme.onErrorContainer
                            )
                        }
                    }
                }

                // Botón de Enviar
                Button(
                    onClick = {
                        // Validación
                        when {
                            email.isEmpty() -> {
                                showError = true
                                showSuccess = false
                                message = "Por favor ingresa tu email"
                            }
                            !email.contains("@") || !email.contains(".") -> {
                                showError = true
                                showSuccess = false
                                message = "Por favor ingresa un email válido"
                            }
                            else -> {
                                // Simular envío de email
                                isLoading = true
                                coroutineScope.launch {
                                    delay(2000)
                                    isLoading = false
                                    showError = false
                                    showSuccess = true
                                    message = "Se han enviado las instrucciones a tu email"
                                    delay(1500)
                                    emailSent = true
                                }
                            }
                        }
                    },
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(56.dp),
                    shape = RoundedCornerShape(12.dp),
                    enabled = email.isNotEmpty() && !isLoading
                ) {
                    if (isLoading) {
                        CircularProgressIndicator(
                            modifier = Modifier.size(24.dp),
                            color = MaterialTheme.colorScheme.onPrimary
                        )
                    } else {
                        Icon(
                            Icons.AutoMirrored.Filled.Send,
                            contentDescription = null,
                            modifier = Modifier.padding(end = 8.dp)
                        )
                        Text(
                            text = "Enviar Instrucciones",
                            fontSize = 18.sp,
                            fontWeight = FontWeight.Medium
                        )
                    }
                }
            } else {
                // Pantalla de confirmación
                Card(
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(16.dp),
                    colors = CardDefaults.cardColors(
                        containerColor = MaterialTheme.colorScheme.secondaryContainer
                    )
                ) {
                    Column(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(20.dp),
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        Icon(
                            Icons.Default.Info,
                            contentDescription = null,
                            tint = MaterialTheme.colorScheme.onSecondaryContainer,
                            modifier = Modifier.size(24.dp)
                        )
                        Spacer(modifier = Modifier.height(12.dp))
                        Text(
                            text = "Revisa tu bandeja de entrada",
                            fontWeight = FontWeight.Bold,
                            fontSize = 16.sp,
                            color = MaterialTheme.colorScheme.onSecondaryContainer
                        )
                        Spacer(modifier = Modifier.height(8.dp))
                        Text(
                            text = "Si no ves el correo, revisa tu carpeta de spam o correo no deseado",
                            fontSize = 14.sp,
                            textAlign = TextAlign.Center,
                            color = MaterialTheme.colorScheme.onSecondaryContainer.copy(alpha = 0.8f)
                        )
                    }
                }

                Spacer(modifier = Modifier.height(24.dp))

                // Botón para abrir app de email (simulado)
                OutlinedButton(
                    onClick = { /* Abrir app de email */ },
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(56.dp),
                    shape = RoundedCornerShape(12.dp)
                ) {
                    Icon(
                        Icons.Default.Email,
                        contentDescription = null,
                        modifier = Modifier.padding(end = 8.dp)
                    )
                    Text(
                        text = "Abrir Email",
                        fontSize = 16.sp
                    )
                }
            }

            Spacer(modifier = Modifier.height(24.dp))

            // Botón de volver al login
            OutlinedButton(
                onClick = onNavigateToLogin,
                modifier = Modifier
                    .fillMaxWidth()
                    .height(56.dp),
                shape = RoundedCornerShape(12.dp)
            ) {
                Icon(
                    Icons.AutoMirrored.Filled.Login,
                    contentDescription = null,
                    modifier = Modifier.padding(end = 8.dp)
                )
                Text(
                    text = "Volver al Login",
                    fontSize = 16.sp
                )
            }

            Spacer(modifier = Modifier.weight(1f))

            // Texto de ayuda adicional
            Column(
                horizontalAlignment = Alignment.CenterHorizontally,
                modifier = Modifier.padding(vertical = 16.dp)
            ) {
                Text(
                    text = "¿No recibiste el correo?",
                    fontSize = 12.sp,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
                TextButton(
                    onClick = {
                        if (emailSent) {
                            // Resetear para reenviar
                            emailSent = false
                            showSuccess = false
                            message = ""
                        }
                    },
                    enabled = emailSent
                ) {
                    Text(
                        text = "Reenviar correo",
                        fontSize = 14.sp
                    )
                }

                Spacer(modifier = Modifier.height(8.dp))

                Text(
                    text = "¿Necesitas ayuda?",
                    fontSize = 12.sp,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
                TextButton(onClick = { /* Abrir soporte */ }) {
                    Text(
                        text = "Contactar soporte",
                        fontSize = 14.sp
                    )
                }
            }
        }
    }
}