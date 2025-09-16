package com.example.appsemana1.auth

import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel

data class AppUser(
    val name: String,
    val email: String,
    var password: String
)

sealed class AuthResult {
    data class Ok(val user: AppUser): AuthResult()
    data class Error(val message: String): AuthResult()
}

class AuthViewModel : ViewModel() {

    private val _users = mutableStateListOf<AppUser>(
        AppUser(name = "Admin Demo", email = "admin@demo.com", password = "123456")
    )
    val users: List<AppUser> get() = _users

    val currentUser = mutableStateOf<AppUser?>(null)

    private fun normalizeEmail(email: String) = email.trim().lowercase()

    fun register(name: String, email: String, password: String): AuthResult {
        val em = normalizeEmail(email)
        if (name.isBlank()) return AuthResult.Error("El nombre es obligatorio")
        if (!em.contains("@")) return AuthResult.Error("Email inválido")
        if (password.length < 6) return AuthResult.Error("La contraseña debe tener al menos 6 caracteres")

        if (_users.any { it.email == em }) {
            return AuthResult.Error("Ya existe una cuenta con ese email")
        }

        val newUser = AppUser(name = name.trim(), email = em, password = password)
        _users.add(newUser)
        return AuthResult.Ok(newUser)
    }

    fun login(email: String, password: String): AuthResult {
        val em = normalizeEmail(email)
        val user = _users.firstOrNull { it.email == em }
            ?: return AuthResult.Error("No existe una cuenta con ese email")
        if (user.password != password) return AuthResult.Error("Contraseña incorrecta")
        currentUser.value = user
        return AuthResult.Ok(user)
    }

    fun resetPassword(email: String, newPassword: String): AuthResult {
        val em = normalizeEmail(email)
        val user = _users.firstOrNull { it.email == em }
        return if (user != null) {
            user.password = newPassword
            AuthResult.Ok(user)
        } else {
            AuthResult.Error("No existe un usuario con ese correo")
        }
    }

    fun logout() {
        currentUser.value = null
    }
}
