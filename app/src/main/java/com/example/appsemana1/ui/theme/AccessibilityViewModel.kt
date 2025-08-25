package com.example.appsemana1.ui.theme

import android.content.Context
import androidx.lifecycle.ViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow

class AccessibilityViewModel : ViewModel() {

    private val _settings = MutableStateFlow(AccessibilitySettings())
    val settings: StateFlow<AccessibilitySettings> = _settings.asStateFlow()

    /** Carga inicial (SharedPreferences simple para no agregar dependencias) */
    fun initialize(context: Context) {
        val sp = context.getSharedPreferences("accessibility_prefs", Context.MODE_PRIVATE)
        val typeOrdinal = sp.getInt("colorblindType", ColorblindType.NONE.ordinal)
        val fontOrdinal = sp.getInt("fontSize", FontSize.NORMAL.ordinal)
        val highContrast = sp.getBoolean("highContrast", false)
        val darkMode = sp.getBoolean("darkMode", false)
        val isColorblindMode = sp.getBoolean("isColorblindMode", false)

        _settings.value = AccessibilitySettings(
            isColorblindMode = isColorblindMode,
            colorblindType = ColorblindType.values()[typeOrdinal],
            fontSize = FontSize.values()[fontOrdinal],
            highContrast = highContrast,
            darkMode = darkMode
        )
    }

    /** Guarda y emite nuevos ajustes */
    fun updateSettings(context: Context, newSettings: AccessibilitySettings) {
        _settings.value = newSettings
        val sp = context.getSharedPreferences("accessibility_prefs", Context.MODE_PRIVATE)
        sp.edit()
            .putInt("colorblindType", newSettings.colorblindType.ordinal)
            .putInt("fontSize", newSettings.fontSize.ordinal)
            .putBoolean("highContrast", newSettings.highContrast)
            .putBoolean("darkMode", newSettings.darkMode)
            .putBoolean("isColorblindMode", newSettings.isColorblindMode)
            .apply()
    }
}
