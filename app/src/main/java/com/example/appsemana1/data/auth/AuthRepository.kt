package com.example.appsemana1.data.auth

import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.UserProfileChangeRequest
import kotlinx.coroutines.tasks.await

class AuthRepository {

    // Usamos la API base directamente
    private val authInstance: FirebaseAuth = FirebaseAuth.getInstance()

    suspend fun signIn(email: String, password: String): Result<Unit> {
        return runCatching {
            authInstance.signInWithEmailAndPassword(email, password).await()
            Unit
        }
    }

    suspend fun signUp(fullName: String?, email: String, password: String): Result<Unit> {
        return runCatching {
            authInstance.createUserWithEmailAndPassword(email, password).await()
            if (!fullName.isNullOrBlank()) {
                val req = UserProfileChangeRequest.Builder()
                    .setDisplayName(fullName)
                    .build()
                authInstance.currentUser?.updateProfile(req)?.await()
            }
            Unit
        }
    }

    suspend fun sendPasswordReset(email: String): Result<Unit> {
        return runCatching {
            authInstance.sendPasswordResetEmail(email).await()
            Unit
        }
    }

    fun signOut() = authInstance.signOut()
    fun isLoggedIn(): Boolean = authInstance.currentUser != null
}
