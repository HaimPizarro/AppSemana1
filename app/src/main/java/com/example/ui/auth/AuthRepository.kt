package com.example.ui.auth

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.appsemana1.data.auth.AuthRepository
import kotlinx.coroutines.launch
import androidx.compose.runtime.mutableStateOf

data class AuthUiState(
    val loading: Boolean = false,
    val error: String? = null,
    val success: Boolean = false
)

class AuthViewModel(
    private val repo: AuthRepository = AuthRepository()
) : ViewModel() {

    var loginState = mutableStateOf(AuthUiState()); private set
    var registerState = mutableStateOf(AuthUiState()); private set
    var resetState = mutableStateOf(AuthUiState()); private set

    fun login(email: String, password: String) {
        loginState.value = AuthUiState(loading = true)
        viewModelScope.launch {
            repo.signIn(email, password)
                .onSuccess { loginState.value = AuthUiState(success = true) }
                .onFailure { loginState.value = AuthUiState(error = it.message) }
        }
    }

    fun register(fullName: String?, email: String, password: String) {
        registerState.value = AuthUiState(loading = true)
        viewModelScope.launch {
            repo.signUp(fullName, email, password)
                .onSuccess { registerState.value = AuthUiState(success = true) }
                .onFailure { registerState.value = AuthUiState(error = it.message) }
        }
    }

    fun resetPassword(email: String) {
        resetState.value = AuthUiState(loading = true)
        viewModelScope.launch {
            repo.sendPasswordReset(email)
                .onSuccess { resetState.value = AuthUiState(success = true) }
                .onFailure { resetState.value = AuthUiState(error = it.message) }
        }
    }

    fun signOut() = repo.signOut()
    fun isLoggedIn() = repo.isLoggedIn()
}
