package com.example.appsemana1.ui.theme

import android.app.Activity
import android.os.Build
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.compositionLocalOf
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext

// ========== CONFIGURACIÓN DE ACCESIBILIDAD ==========

// Estados de accesibilidad
data class AccessibilitySettings(
    val isColorblindMode: Boolean = false,
    val colorblindType: ColorblindType = ColorblindType.NONE,
    val fontSize: FontSize = FontSize.NORMAL,
    val highContrast: Boolean = false,
    val darkMode: Boolean = false
)

enum class ColorblindType {
    NONE, PROTANOPIA, DEUTERANOPIA, TRITANOPIA, MONOCHROMATIC
}

enum class FontSize(val multiplier: Float) {
    SMALL(0.85f),
    NORMAL(1.0f),
    LARGE(1.15f),
    EXTRA_LARGE(1.3f),
    ACCESSIBILITY(1.5f)  // Tamaño extra para accesibilidad extrema
}

// CompositionLocal para acceder a configuraciones desde cualquier lugar
val LocalAccessibilitySettings = compositionLocalOf { AccessibilitySettings() }

// ========== ESQUEMAS DE COLOR ORIGINALES (MEJORADOS) ==========

private val DarkColorScheme = darkColorScheme(
    primary = Purple80,
    secondary = PurpleGrey80,
    tertiary = Pink80,
    background = Color(0xFF121212),
    surface = Color(0xFF1E1E1E),
    onPrimary = Color(0xFF000000),
    onSecondary = Color(0xFF000000),
    onTertiary = Color(0xFF000000),
    onBackground = Color(0xFFE6E1E5),
    onSurface = Color(0xFFE6E1E5),
)

private val LightColorScheme = lightColorScheme(
    primary = Purple40,
    secondary = PurpleGrey40,
    tertiary = Pink40,
    background = Color(0xFFFFFBFE),
    surface = Color(0xFFFFFBFE),
    onPrimary = Color.White,
    onSecondary = Color.White,
    onTertiary = Color.White,
    onBackground = Color(0xFF1C1B1F),
    onSurface = Color(0xFF1C1B1F),
)

// ========== ESQUEMAS PARA DALTONISMO ==========

private val ProtanopiaLightScheme = lightColorScheme(
    primary = ProtanopiaColors.Primary,
    primaryContainer = Color(0xFFE3F2FD),
    onPrimary = Color.White,
    onPrimaryContainer = ProtanopiaColors.PrimaryVariant,
    secondary = ProtanopiaColors.Secondary,
    secondaryContainer = Color(0xFFFFF8E1),
    onSecondary = Color.White,
    onSecondaryContainer = ProtanopiaColors.SecondaryVariant,
    tertiary = Color(0xFF00ACC1),
    background = ProtanopiaColors.Background,
    surface = ProtanopiaColors.Surface,
    onBackground = Color(0xFF1A1A1A),
    onSurface = Color(0xFF1A1A1A),
    error = ProtanopiaColors.Error,
    onError = Color.White,
    outline = Color(0xFF737373),
    outlineVariant = Color(0xFFBDBDBD)
)

private val DeuteranopiaLightScheme = lightColorScheme(
    primary = DeuteranopiaColors.Primary,
    primaryContainer = Color(0xFFF3E5F5),
    onPrimary = Color.White,
    onPrimaryContainer = DeuteranopiaColors.PrimaryVariant,
    secondary = DeuteranopiaColors.Secondary,
    secondaryContainer = Color(0xFFE3F2FD),
    onSecondary = Color.White,
    onSecondaryContainer = DeuteranopiaColors.SecondaryVariant,
    tertiary = Color(0xFF00796B),
    background = DeuteranopiaColors.Background,
    surface = DeuteranopiaColors.Surface,
    onBackground = Color(0xFF1A1A1A),
    onSurface = Color(0xFF1A1A1A),
    error = DeuteranopiaColors.Error,
    onError = Color.White,
    outline = Color(0xFF737373),
    outlineVariant = Color(0xFFBDBDBD)
)

private val TritanopiaLightScheme = lightColorScheme(
    primary = TritanopiaColors.Primary,
    primaryContainer = Color(0xFFFBE9E7),
    onPrimary = Color.White,
    onPrimaryContainer = TritanopiaColors.PrimaryVariant,
    secondary = TritanopiaColors.Secondary,
    secondaryContainer = Color(0xFFE8F5E8),
    onSecondary = Color.White,
    onSecondaryContainer = TritanopiaColors.SecondaryVariant,
    tertiary = Color(0xFFD84315),
    background = TritanopiaColors.Background,
    surface = TritanopiaColors.Surface,
    onBackground = Color(0xFF1A1A1A),
    onSurface = Color(0xFF1A1A1A),
    error = TritanopiaColors.Error,
    onError = Color.White,
    outline = Color(0xFF737373),
    outlineVariant = Color(0xFFBDBDBD)
)

private val MonochromaticLightScheme = lightColorScheme(
    primary = MonochromaticColors.Primary,
    primaryContainer = Color(0xFFE0E0E0),
    onPrimary = Color.White,
    onPrimaryContainer = MonochromaticColors.PrimaryVariant,
    secondary = MonochromaticColors.Secondary,
    secondaryContainer = Color(0xFFF5F5F5),
    onSecondary = Color.White,
    onSecondaryContainer = MonochromaticColors.SecondaryVariant,
    tertiary = Color(0xFF616161),
    background = MonochromaticColors.Background,
    surface = MonochromaticColors.Surface,
    onBackground = Color(0xFF212121),
    onSurface = Color(0xFF212121),
    error = MonochromaticColors.Error,
    onError = Color.White,
    outline = Color(0xFF9E9E9E),
    outlineVariant = Color(0xFFE0E0E0)
)

private val HighContrastLightScheme = lightColorScheme(
    primary = HighContrastColors.Primary,
    primaryContainer = Color.White,
    onPrimary = HighContrastColors.OnPrimary,
    onPrimaryContainer = Color.Black,
    secondary = HighContrastColors.Secondary,
    secondaryContainer = Color.White,
    onSecondary = HighContrastColors.OnSecondary,
    onSecondaryContainer = Color.Black,
    tertiary = Color.Black,
    background = HighContrastColors.Background,
    surface = HighContrastColors.Surface,
    onBackground = HighContrastColors.OnBackground,
    onSurface = HighContrastColors.OnSurface,
    error = HighContrastColors.Error,
    onError = HighContrastColors.OnError,
    outline = Color.Black,
    outlineVariant = Color.Black
)

// ========== FUNCIÓN PRINCIPAL PARA OBTENER ESQUEMA ==========

@Composable
fun getAccessibilityColorScheme(
    settings: AccessibilitySettings,
    darkTheme: Boolean = isSystemInDarkTheme(),
    dynamicColor: Boolean = true
): ColorScheme {
    return when {
        // Prioridad al alto contraste
        settings.highContrast -> HighContrastLightScheme

        // Esquemas para daltonismo
        settings.colorblindType == ColorblindType.PROTANOPIA -> ProtanopiaLightScheme
        settings.colorblindType == ColorblindType.DEUTERANOPIA -> DeuteranopiaLightScheme
        settings.colorblindType == ColorblindType.TRITANOPIA -> TritanopiaLightScheme
        settings.colorblindType == ColorblindType.MONOCHROMATIC -> MonochromaticLightScheme

        // Esquemas dinámicos estándar (Android 12+)
        dynamicColor && Build.VERSION.SDK_INT >= Build.VERSION_CODES.S -> {
            val context = LocalContext.current
            if (darkTheme || settings.darkMode) {
                dynamicDarkColorScheme(context)
            } else {
                dynamicLightColorScheme(context)
            }
        }

        // Esquemas estándar mejorados
        darkTheme || settings.darkMode -> DarkColorScheme
        else -> LightColorScheme
    }
}

// ========== TIPOGRAFÍA ADAPTATIVA ==========

@Composable
fun getAccessibilityTypography(settings: AccessibilitySettings): Typography {
    val baseTypography = Typography
    val multiplier = settings.fontSize.multiplier

    return Typography(
        displayLarge = baseTypography.displayLarge.copy(
            fontSize = baseTypography.displayLarge.fontSize * multiplier
        ),
        displayMedium = baseTypography.displayMedium.copy(
            fontSize = baseTypography.displayMedium.fontSize * multiplier
        ),
        displaySmall = baseTypography.displaySmall.copy(
            fontSize = baseTypography.displaySmall.fontSize * multiplier
        ),
        headlineLarge = baseTypography.headlineLarge.copy(
            fontSize = baseTypography.headlineLarge.fontSize * multiplier
        ),
        headlineMedium = baseTypography.headlineMedium.copy(
            fontSize = baseTypography.headlineMedium.fontSize * multiplier
        ),
        headlineSmall = baseTypography.headlineSmall.copy(
            fontSize = baseTypography.headlineSmall.fontSize * multiplier
        ),
        titleLarge = baseTypography.titleLarge.copy(
            fontSize = baseTypography.titleLarge.fontSize * multiplier
        ),
        titleMedium = baseTypography.titleMedium.copy(
            fontSize = baseTypography.titleMedium.fontSize * multiplier
        ),
        titleSmall = baseTypography.titleSmall.copy(
            fontSize = baseTypography.titleSmall.fontSize * multiplier
        ),
        bodyLarge = baseTypography.bodyLarge.copy(
            fontSize = baseTypography.bodyLarge.fontSize * multiplier
        ),
        bodyMedium = baseTypography.bodyMedium.copy(
            fontSize = baseTypography.bodyMedium.fontSize * multiplier
        ),
        bodySmall = baseTypography.bodySmall.copy(
            fontSize = baseTypography.bodySmall.fontSize * multiplier
        ),
        labelLarge = baseTypography.labelLarge.copy(
            fontSize = baseTypography.labelLarge.fontSize * multiplier
        ),
        labelMedium = baseTypography.labelMedium.copy(
            fontSize = baseTypography.labelMedium.fontSize * multiplier
        ),
        labelSmall = baseTypography.labelSmall.copy(
            fontSize = baseTypography.labelSmall.fontSize * multiplier
        )
    )
}

// ========== TEMA PRINCIPAL MEJORADO ==========

@Composable
fun AppSemana1Theme(
    darkTheme: Boolean = isSystemInDarkTheme(),
    dynamicColor: Boolean = true,
    accessibilitySettings: AccessibilitySettings = AccessibilitySettings(),
    content: @Composable () -> Unit
) {
    val colorScheme = getAccessibilityColorScheme(
        settings = accessibilitySettings,
        darkTheme = darkTheme,
        dynamicColor = dynamicColor
    )

    val typography = getAccessibilityTypography(accessibilitySettings)

    CompositionLocalProvider(
        LocalAccessibilitySettings provides accessibilitySettings
    ) {
        MaterialTheme(
            colorScheme = colorScheme,
            typography = typography,
            content = content
        )
    }
}