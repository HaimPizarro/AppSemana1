package com.example.appsemana1.ui.theme

import androidx.compose.ui.graphics.Color

// ========== COLORES ORIGINALES (MANTENER) ==========
val Purple80 = Color(0xFFD0BCFF)
val PurpleGrey80 = Color(0xFFCCC2DC)
val Pink80 = Color(0xFFEFB8C8)

val Purple40 = Color(0xFF6650a4)
val PurpleGrey40 = Color(0xFF625b71)
val Pink40 = Color(0xFF7D5260)

// ========== COLORES BASE MEJORADOS PARA ACCESIBILIDAD ==========

// Colores normales con mejor contraste
val AccessibleBlue = Color(0xFF1976D2)      // Azul más accesible
val AccessibleBlueLight = Color(0xFF42A5F5)
val AccessibleGreen = Color(0xFF388E3C)     // Verde más accesible
val AccessibleGreenLight = Color(0xFF66BB6A)
val AccessibleRed = Color(0xFFD32F2F)       // Rojo más accesible
val AccessibleRedLight = Color(0xFFEF5350)

// ========== PALETAS PARA DALTONISMO ==========

// Paleta para Protanopia (dificultad con rojos) - Usa azules y amarillos
object ProtanopiaColors {
    val Primary = Color(0xFF1565C0)          // Azul fuerte
    val PrimaryVariant = Color(0xFF0D47A1)   // Azul más oscuro
    val Secondary = Color(0xFFFFA000)        // Amarillo/naranja
    val SecondaryVariant = Color(0xFFFF8F00) // Naranja
    val Surface = Color(0xFFF5F5F5)
    val Background = Color(0xFFFFFFFF)
    val Error = Color(0xFF1565C0)            // Usar azul en lugar de rojo
}

// Paleta para Deuteranopia (dificultad con verdes) - Usa púrpuras y azules
object DeuteranopiaColors {
    val Primary = Color(0xFF7B1FA2)          // Púrpura
    val PrimaryVariant = Color(0xFF4A148C)   // Púrpura oscuro
    val Secondary = Color(0xFF1976D2)        // Azul
    val SecondaryVariant = Color(0xFF0D47A1) // Azul oscuro
    val Surface = Color(0xFFF5F5F5)
    val Background = Color(0xFFFFFFFF)
    val Error = Color(0xFF7B1FA2)            // Púrpura para errores
}

// Paleta para Tritanopia (dificultad con azules) - Usa rojos y verdes
object TritanopiaColors {
    val Primary = Color(0xFFD84315)          // Naranja/rojo
    val PrimaryVariant = Color(0xFFBF360C)   // Rojo oscuro
    val Secondary = Color(0xFF2E7D32)        // Verde
    val SecondaryVariant = Color(0xFF1B5E20) // Verde oscuro
    val Surface = Color(0xFFF5F5F5)
    val Background = Color(0xFFFFFFFF)
    val Error = Color(0xFFAD1457)            // Rosa para errores
}

// Paleta Monocromática (escala de grises)
object MonochromaticColors {
    val Primary = Color(0xFF424242)          // Gris oscuro
    val PrimaryVariant = Color(0xFF212121)   // Gris muy oscuro
    val Secondary = Color(0xFF757575)        // Gris medio
    val SecondaryVariant = Color(0xFF616161) // Gris medio-oscuro
    val Surface = Color(0xFFFAFAFA)          // Gris muy claro
    val Background = Color(0xFFFFFFFF)       // Blanco
    val Error = Color(0xFF424242)            // Gris oscuro para errores
}

// Alto Contraste (Negro y Blanco)
object HighContrastColors {
    val Primary = Color(0xFF000000)          // Negro puro
    val PrimaryVariant = Color(0xFF000000)
    val Secondary = Color(0xFF424242)        // Gris oscuro
    val SecondaryVariant = Color(0xFF212121)
    val Surface = Color(0xFFFFFFFF)          // Blanco puro
    val Background = Color(0xFFFFFFFF)
    val Error = Color(0xFF000000)
    val OnPrimary = Color(0xFFFFFFFF)
    val OnSecondary = Color(0xFFFFFFFF)
    val OnSurface = Color(0xFF000000)
    val OnBackground = Color(0xFF000000)
    val OnError = Color(0xFFFFFFFF)
}

// ========== COLORES PARA MODO OSCURO ACCESIBLE ==========

// Modo oscuro con mejor contraste para daltonismo
object DarkAccessibilityColors {
    val Primary = Color(0xFF90CAF9)          // Azul claro
    val Secondary = Color(0xFFFFCC02)        // Amarillo
    val Surface = Color(0xFF121212)          // Gris muy oscuro
    val Background = Color(0xFF000000)       // Negro
    val Error = Color(0xFF90CAF9)            // Azul claro para errores
}

// ========== COLORES DE ESTADO UNIVERSALES ==========

// Colores que funcionan para todos los tipos de daltonismo
object UniversalColors {
    val Success = Color(0xFF2E7D32)          // Verde oscuro
    val Warning = Color(0xFFED6C02)          // Naranja
    val Info = Color(0xFF0288D1)             // Azul
    val Neutral = Color(0xFF6B7280)          // Gris neutro
}

// ========== COLORES DE SOPORTE ==========

// Transparencias útiles
val BlackAlpha12 = Color(0x1F000000)
val BlackAlpha38 = Color(0x61000000)
val BlackAlpha54 = Color(0x8A000000)
val WhiteAlpha12 = Color(0x1FFFFFFF)
val WhiteAlpha38 = Color(0x61FFFFFF)