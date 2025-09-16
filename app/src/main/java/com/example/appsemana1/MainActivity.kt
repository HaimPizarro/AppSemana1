package com.example.appsemana1

import android.os.Bundle
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.lifecycle.viewmodel.compose.viewModel
import com.google.firebase.FirebaseApp
import com.google.firebase.auth.FirebaseAuth
import com.example.appsemana1.ui.theme.*
import com.example.appsemana1.auth.AuthViewModel   // ⬅️ Importante

class MainActivity : ComponentActivity() {

    private val tag = "FIREBASE_TEST"
    private val auth by lazy { FirebaseAuth.getInstance() }

    // Listener para ver cambios de sesión en Logcat (Firebase; opcional mientras uses auth local)
    private val authListener = FirebaseAuth.AuthStateListener { firebaseAuth ->
        val user = firebaseAuth.currentUser
        if (user != null) {
            Log.d(tag, "AuthState → LOGGED IN, uid=${user.uid}, email=${user.email}")
        } else {
            Log.d(tag, "AuthState → LOGGED OUT")
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()

        // Inicializa Firebase (opcional si luego migras a Firebase Auth)
        val app = FirebaseApp.initializeApp(this)
        if (app != null) {
            Log.d(tag, "Firebase inicializado correctamente ✅ (name=${app.name})")
        } else {
            Log.e(tag, "Firebase NO se inicializó ❌ (revisa google-services.json y el plugin)")
        }
        Log.d(tag, "Usuario actual al iniciar: ${auth.currentUser}")

        setContent {
            // ViewModels
            val accessibilityViewModel: AccessibilityViewModel = viewModel()
            val authViewModel: AuthViewModel = viewModel()   // ⬅️ Auth local en memoria

            val context = LocalContext.current
            LaunchedEffect(Unit) {
                accessibilityViewModel.initialize(context)
            }

            val accessibilitySettings by accessibilityViewModel
                .settings
                .collectAsState(initial = AccessibilitySettings())

            AppSemana1Theme(accessibilitySettings = accessibilitySettings) {
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    AppNavigation(
                        accessibilityViewModel = accessibilityViewModel,
                        authViewModel = authViewModel     // ⬅️ pásalo a la navegación
                    )
                }
            }
        }
    }

    override fun onStart() {
        super.onStart()
        // Empieza a escuchar cambios de sesión de Firebase (solo logs)
        auth.addAuthStateListener(authListener)
    }

    override fun onStop() {
        super.onStop()
        // Deja de escuchar para evitar fugas
        auth.removeAuthStateListener(authListener)
    }
}
