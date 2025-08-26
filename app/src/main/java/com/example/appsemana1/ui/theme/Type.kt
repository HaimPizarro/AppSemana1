package com.example.appsemana1.ui.theme

import androidx.compose.material3.Typography
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.sp


// Fuente predeterminada
val DefaultFontFamily = FontFamily.Default

/*
val AccessibilityFontFamily = FontFamily(
    Font(R.font.open_sans_regular, FontWeight.Normal),
    Font(R.font.open_sans_bold, FontWeight.Bold),
    Font(R.font.open_sans_medium, FontWeight.Medium)
)
*/


val Typography = Typography(

    // Display styles - Títulos muy grandes
    displayLarge = TextStyle(
        fontFamily = DefaultFontFamily,
        fontWeight = FontWeight.Normal,
        fontSize = 57.sp,
        lineHeight = 64.sp,
        letterSpacing = (-0.25).sp
    ),
    displayMedium = TextStyle(
        fontFamily = DefaultFontFamily,
        fontWeight = FontWeight.Normal,
        fontSize = 45.sp,
        lineHeight = 52.sp,
        letterSpacing = 0.sp
    ),
    displaySmall = TextStyle(
        fontFamily = DefaultFontFamily,
        fontWeight = FontWeight.Normal,
        fontSize = 36.sp,
        lineHeight = 44.sp,
        letterSpacing = 0.sp
    ),

    // Headline styles - Títulos grandes
    headlineLarge = TextStyle(
        fontFamily = DefaultFontFamily,
        fontWeight = FontWeight.Normal,
        fontSize = 32.sp,
        lineHeight = 40.sp,
        letterSpacing = 0.sp
    ),
    headlineMedium = TextStyle(
        fontFamily = DefaultFontFamily,
        fontWeight = FontWeight.Normal,
        fontSize = 28.sp,
        lineHeight = 36.sp,
        letterSpacing = 0.sp
    ),
    headlineSmall = TextStyle(
        fontFamily = DefaultFontFamily,
        fontWeight = FontWeight.Normal,
        fontSize = 24.sp,
        lineHeight = 32.sp,
        letterSpacing = 0.sp
    ),

    // Title styles - Títulos medianos
    titleLarge = TextStyle(
        fontFamily = DefaultFontFamily,
        fontWeight = FontWeight.Normal,
        fontSize = 22.sp,
        lineHeight = 28.sp,
        letterSpacing = 0.sp
    ),
    titleMedium = TextStyle(
        fontFamily = DefaultFontFamily,
        fontWeight = FontWeight.Medium,
        fontSize = 16.sp,
        lineHeight = 24.sp,
        letterSpacing = 0.15.sp
    ),
    titleSmall = TextStyle(
        fontFamily = DefaultFontFamily,
        fontWeight = FontWeight.Medium,
        fontSize = 14.sp,
        lineHeight = 20.sp,
        letterSpacing = 0.1.sp
    ),

    // Body styles - Texto principal
    bodyLarge = TextStyle(
        fontFamily = DefaultFontFamily,
        fontWeight = FontWeight.Normal,
        fontSize = 18.sp,
        lineHeight = 26.sp,
        letterSpacing = 0.5.sp
    ),
    bodyMedium = TextStyle(
        fontFamily = DefaultFontFamily,
        fontWeight = FontWeight.Normal,
        fontSize = 16.sp,
        lineHeight = 22.sp,
        letterSpacing = 0.25.sp
    ),
    bodySmall = TextStyle(
        fontFamily = DefaultFontFamily,
        fontWeight = FontWeight.Normal,
        fontSize = 14.sp,
        lineHeight = 18.sp,
        letterSpacing = 0.4.sp
    ),

    // Label styles - Etiquetas y botones
    labelLarge = TextStyle(
        fontFamily = DefaultFontFamily,
        fontWeight = FontWeight.Medium,
        fontSize = 16.sp,        //
        lineHeight = 22.sp,
        letterSpacing = 0.1.sp
    ),
    labelMedium = TextStyle(
        fontFamily = DefaultFontFamily,
        fontWeight = FontWeight.Medium,
        fontSize = 14.sp,
        lineHeight = 18.sp,
        letterSpacing = 0.5.sp
    ),
    labelSmall = TextStyle(
        fontFamily = DefaultFontFamily,
        fontWeight = FontWeight.Medium,
        fontSize = 12.sp,
        lineHeight = 16.sp,
        letterSpacing = 0.5.sp
    )
)


// Tipografía para alto contraste
val HighContrastTypography = Typography.copy(
    bodyLarge = Typography.bodyLarge.copy(
        fontWeight = FontWeight.Medium,
        fontSize = 20.sp,
        lineHeight = 28.sp
    ),
    bodyMedium = Typography.bodyMedium.copy(
        fontWeight = FontWeight.Medium,
        fontSize = 18.sp,
        lineHeight = 24.sp
    ),
    bodySmall = Typography.bodySmall.copy(
        fontWeight = FontWeight.Medium,
        fontSize = 16.sp,
        lineHeight = 20.sp
    ),
    labelLarge = Typography.labelLarge.copy(
        fontWeight = FontWeight.Bold,
        fontSize = 18.sp,
        lineHeight = 24.sp
    ),
    labelMedium = Typography.labelMedium.copy(
        fontWeight = FontWeight.Bold,
        fontSize = 16.sp,
        lineHeight = 20.sp
    )
)

// Tipografía para dislexia (mejor espaciado y claridad)
val DyslexiaFriendlyTypography = Typography.copy(
    bodyLarge = Typography.bodyLarge.copy(
        fontSize = 20.sp,
        lineHeight = 30.sp,
        letterSpacing = 1.sp
    ),
    bodyMedium = Typography.bodyMedium.copy(
        fontSize = 18.sp,
        lineHeight = 26.sp,
        letterSpacing = 0.8.sp
    ),
    bodySmall = Typography.bodySmall.copy(
        fontSize = 16.sp,
        lineHeight = 22.sp,
        letterSpacing = 0.6.sp
    )
)


// Función para obtener el tamaño mínimo recomendado según WCAG
object AccessibleTextSizes {
    val MinimumTouch = 44.sp        // Tamaño mínimo para elementos tocables
    val MinimumBody = 16.sp         // Tamaño mínimo para texto del cuerpo
    val RecommendedBody = 18.sp     // Tamaño recomendado para mejor legibilidad
    val MinimumLabel = 14.sp        // Tamaño mínimo para etiquetas
    val LargeText = 24.sp          // Considerado texto grande por WCAG
}

// Espaciados recomendados para accesibilidad
object AccessibleSpacing {
    val MinimumLineHeight = 1.5f    // Altura de línea mínima recomendada
    val RecommendedLineHeight = 1.6f // Altura de línea recomendada
    val LetterSpacingNormal = 0.5.sp // Espaciado normal entre letras
    val LetterSpacingWide = 1.0.sp   // Espaciado amplio para dislexia
}