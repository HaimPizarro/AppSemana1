package com.example.appsemana1.ui.theme

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Accessibility
import androidx.compose.material.icons.filled.ColorLens
import androidx.compose.material.icons.filled.Contrast
import androidx.compose.material.icons.filled.DarkMode
import androidx.compose.material.icons.filled.LightMode
import androidx.compose.material.icons.filled.FormatSize
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Checkbox
import androidx.compose.material3.FilterChip
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.RadioButton
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.hapticfeedback.HapticFeedbackType
import androidx.compose.ui.platform.LocalHapticFeedback
import androidx.compose.ui.semantics.contentDescription
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.graphics.vector.ImageVector

@Composable
fun rememberAccessibilityManager(): AccessibilityManager {
    return remember { AccessibilityManager() }
}

class AccessibilityManager {

    @Composable
    fun AccessibilityFloatingButton(
        onAccessibilityClick: () -> Unit,
        modifier: Modifier = Modifier
    ) {
        val haptic = LocalHapticFeedback.current

        FloatingActionButton(
            onClick = {
                haptic.performHapticFeedback(HapticFeedbackType.LongPress)
                onAccessibilityClick()
            },
            modifier = modifier
                .size(56.dp)
                .semantics {
                    contentDescription =
                        "Botón de configuración de accesibilidad. Presiona para personalizar la aplicación según tus necesidades visuales"
                },
            containerColor = MaterialTheme.colorScheme.primaryContainer,
            contentColor = MaterialTheme.colorScheme.onPrimaryContainer
        ) {
            Icon(
                Icons.Default.Accessibility,
                contentDescription = "Configuración de accesibilidad",
                modifier = Modifier.size(24.dp)
            )
        }
    }

    @Composable
    fun AccessibilityIconButton(
        onAccessibilityClick: () -> Unit,
        modifier: Modifier = Modifier
    ) {
        val haptic = LocalHapticFeedback.current

        IconButton(
            onClick = {
                haptic.performHapticFeedback(HapticFeedbackType.LongPress)
                onAccessibilityClick()
            },
            modifier = modifier.semantics {
                contentDescription = "Botón de configuración de accesibilidad"
            }
        ) {
            Icon(
                Icons.Default.Accessibility,
                contentDescription = "Configuración de accesibilidad",
                tint = MaterialTheme.colorScheme.onSurface,
                modifier = Modifier.size(28.dp)
            )
        }
    }

    @Composable
    fun AccessibilityDialog(
        currentSettings: AccessibilitySettings,
        onSettingsChanged: (AccessibilitySettings) -> Unit,
        onDismiss: () -> Unit
    ) {
        val haptic = LocalHapticFeedback.current

        AlertDialog(
            onDismissRequest = onDismiss,
            title = {
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    modifier = Modifier.padding(bottom = 8.dp)
                ) {
                    Icon(
                        Icons.Default.Accessibility,
                        contentDescription = null,
                        modifier = Modifier
                            .size(28.dp)
                            .padding(end = 12.dp),
                        tint = MaterialTheme.colorScheme.primary
                    )
                    Text(
                        "Configuración de Accesibilidad",
                        fontWeight = FontWeight.Bold,
                        fontSize = 20.sp,
                        color = MaterialTheme.colorScheme.onSurface
                    )
                }
            },
            text = {
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .heightIn(max = 500.dp)
                        .verticalScroll(rememberScrollState())
                ) {
                    Text(
                        "Personaliza la aplicación según tus necesidades:",
                        fontSize = 16.sp,
                        color = MaterialTheme.colorScheme.onSurfaceVariant,
                        modifier = Modifier.padding(bottom = 20.dp)
                    )

                    AccessibilitySection(
                        title = "Visión de colores",
                        description = "¿Tienes dificultades para distinguir colores?",
                        icon = Icons.Default.ColorLens
                    ) {
                        ColorblindOptions(
                            currentSettings = currentSettings,
                            onSettingsChanged = onSettingsChanged
                        )
                    }

                    Spacer(modifier = Modifier.height(24.dp))

                    AccessibilitySection(
                        title = "Tamaño del texto",
                        description = "Ajusta el tamaño para mejor legibilidad",
                        icon = Icons.Default.FormatSize
                    ) {
                        FontSizeOptions(
                            currentSettings = currentSettings,
                            onSettingsChanged = onSettingsChanged
                        )
                    }

                    Spacer(modifier = Modifier.height(24.dp))

                    AccessibilitySection(
                        title = "Contraste visual",
                        description = "Mejora la visibilidad con alto contraste",
                        icon = Icons.Default.Contrast
                    ) {
                        ContrastOptions(
                            currentSettings = currentSettings,
                            onSettingsChanged = onSettingsChanged
                        )
                    }

                    Spacer(modifier = Modifier.height(24.dp))

                    AccessibilitySection(
                        title = "Tema visual",
                        description = "Modo claro u oscuro",
                        icon = Icons.Default.DarkMode
                    ) {
                        ThemeOptions(
                            currentSettings = currentSettings,
                            onSettingsChanged = onSettingsChanged
                        )
                    }
                }
            },
            confirmButton = {
                Button(
                    onClick = {
                        haptic.performHapticFeedback(HapticFeedbackType.LongPress)
                        onDismiss()
                    }
                ) {
                    Text(
                        "Aplicar Cambios",
                        fontSize = 16.sp,
                        fontWeight = FontWeight.Medium
                    )
                }
            },
            dismissButton = {
                OutlinedButton(
                    onClick = {
                        haptic.performHapticFeedback(HapticFeedbackType.LongPress)
                        onSettingsChanged(AccessibilitySettings())
                        onDismiss()
                    }
                ) {
                    Text("Restablecer", fontSize = 16.sp)
                }
            },
            containerColor = MaterialTheme.colorScheme.surface,
            tonalElevation = 6.dp
        )
    }
}

/* =================== AUXILIARES =================== */

@Composable
private fun AccessibilitySection(
    title: String,
    description: String,
    icon: ImageVector,
    content: @Composable () -> Unit
) {
    Column {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier.padding(bottom = 8.dp)
        ) {
            Icon(
                icon,
                contentDescription = null,
                modifier = Modifier
                    .size(20.dp)
                    .padding(end = 8.dp),
                tint = MaterialTheme.colorScheme.primary
            )
            Text(
                title,
                fontWeight = FontWeight.Bold,
                fontSize = 16.sp,
                color = MaterialTheme.colorScheme.onSurface
            )
        }

        Text(
            description,
            fontSize = 14.sp,
            color = MaterialTheme.colorScheme.onSurfaceVariant,
            modifier = Modifier.padding(bottom = 12.dp)
        )

        content()
    }
}

@Composable
private fun ColorblindOptions(
    currentSettings: AccessibilitySettings,
    onSettingsChanged: (AccessibilitySettings) -> Unit
) {
    val haptic = LocalHapticFeedback.current

    val colorblindOptions = listOf(
        ColorblindType.NONE to "Sin dificultades",
        ColorblindType.PROTANOPIA to "Dificultad con rojos",
        ColorblindType.DEUTERANOPIA to "Dificultad con verdes",
        ColorblindType.TRITANOPIA to "Dificultad con azules",
        ColorblindType.MONOCHROMATIC to "Solo veo en escala de grises"
    )

    colorblindOptions.forEach { (type, label) ->
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(vertical = 2.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            RadioButton(
                selected = currentSettings.colorblindType == type,
                onClick = {
                    haptic.performHapticFeedback(HapticFeedbackType.LongPress)
                    onSettingsChanged(
                        currentSettings.copy(
                            colorblindType = type,
                            isColorblindMode = type != ColorblindType.NONE
                        )
                    )
                }
            )
            Text(
                text = label,
                fontSize = 14.sp,
                color = MaterialTheme.colorScheme.onSurface,
                modifier = Modifier.padding(start = 12.dp)
            )
        }
    }
}

@Composable
private fun FontSizeOptions(
    currentSettings: AccessibilitySettings,
    onSettingsChanged: (AccessibilitySettings) -> Unit
) {
    val haptic = LocalHapticFeedback.current

    val fontSizeOptions = listOf(
        FontSize.SMALL to "Pequeño",
        FontSize.NORMAL to "Normal",
        FontSize.LARGE to "Grande",
        FontSize.EXTRA_LARGE to "Extra Grande",
        FontSize.ACCESSIBILITY to "Máxima accesibilidad"
    )

    fontSizeOptions.forEach { (size, label) ->
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(vertical = 2.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            RadioButton(
                selected = currentSettings.fontSize == size,
                onClick = {
                    haptic.performHapticFeedback(HapticFeedbackType.LongPress)
                    onSettingsChanged(currentSettings.copy(fontSize = size))
                }
            )
            Text(
                text = label,
                fontSize = (14 * size.multiplier).sp,
                color = MaterialTheme.colorScheme.onSurface,
                modifier = Modifier.padding(start = 12.dp)
            )
        }
    }
}

@Composable
private fun ContrastOptions(
    currentSettings: AccessibilitySettings,
    onSettingsChanged: (AccessibilitySettings) -> Unit
) {
    val haptic = LocalHapticFeedback.current

    Card(
        modifier = Modifier.fillMaxWidth(),
        colors = CardDefaults.cardColors(
            containerColor = if (currentSettings.highContrast) {
                MaterialTheme.colorScheme.primaryContainer
            } else {
                MaterialTheme.colorScheme.surfaceVariant
            }
        )
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Checkbox(
                checked = currentSettings.highContrast,
                onCheckedChange = {
                    haptic.performHapticFeedback(HapticFeedbackType.LongPress)
                    onSettingsChanged(currentSettings.copy(highContrast = it))
                }
            )
            Column(
                modifier = Modifier.padding(start = 12.dp)
            ) {
                Text(
                    "Alto contraste",
                    fontSize = 16.sp,
                    fontWeight = FontWeight.Medium,
                    color = MaterialTheme.colorScheme.onSurface
                )
                Text(
                    "Colores en blanco y negro para máxima visibilidad",
                    fontSize = 14.sp,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
            }
        }
    }
}

@Composable
private fun ThemeOptions(
    currentSettings: AccessibilitySettings,
    onSettingsChanged: (AccessibilitySettings) -> Unit
) {
    val haptic = LocalHapticFeedback.current

    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceEvenly
    ) {
        FilterChip(
            onClick = {
                haptic.performHapticFeedback(HapticFeedbackType.LongPress)
                onSettingsChanged(currentSettings.copy(darkMode = false))
            },
            label = { Text("Modo claro", fontSize = 14.sp) },
            selected = !currentSettings.darkMode,
            leadingIcon = if (!currentSettings.darkMode) {
                { Icon(Icons.Default.LightMode, contentDescription = null, modifier = Modifier.size(16.dp)) }
            } else null
        )

        FilterChip(
            onClick = {
                haptic.performHapticFeedback(HapticFeedbackType.LongPress)
                onSettingsChanged(currentSettings.copy(darkMode = true))
            },
            label = { Text("Modo oscuro", fontSize = 14.sp) },
            selected = currentSettings.darkMode,
            leadingIcon = if (currentSettings.darkMode) {
                { Icon(Icons.Default.DarkMode, contentDescription = null, modifier = Modifier.size(16.dp)) }
            } else null
        )
    }
}
