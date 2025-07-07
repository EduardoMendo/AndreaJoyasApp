package com.example.andreajoyas.viewmodel

import androidx.lifecycle.ViewModel
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.SetOptions
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow

class AuthViewModel : ViewModel() {

    private val auth: FirebaseAuth = FirebaseAuth.getInstance()
    private val db: FirebaseFirestore = FirebaseFirestore.getInstance()

    private val _loginState = MutableStateFlow<String?>(null)
    val loginState: StateFlow<String?> = _loginState

    private val _registerState = MutableStateFlow<String?>(null)
    val registerState: StateFlow<String?> = _registerState

    private val _nombre = MutableStateFlow("")
    private val _apellido = MutableStateFlow("")
    private val _telefono = MutableStateFlow("")
    private val _direccion = MutableStateFlow("")

    val nombre: StateFlow<String> = _nombre
    val apellido: StateFlow<String> = _apellido
    val telefono: StateFlow<String> = _telefono
    val direccion: StateFlow<String> = _direccion

    fun onNombreChanged(value: String) { _nombre.value = value }
    fun onApellidoChanged(value: String) { _apellido.value = value }
    fun onTelefonoChanged(value: String) { _telefono.value = value }
    fun onDireccionChanged(value: String) { _direccion.value = value }

    fun login(email: String, password: String) {
        if (email.isBlank() || password.isBlank()) {
            _loginState.value = "Correo o contraseña incorrecto"
            return
        }

        auth.signInWithEmailAndPassword(email, password)
            .addOnCompleteListener { task ->
                _loginState.value = if (task.isSuccessful) "success" else "Correo o contraseña incorrecto"
            }
    }

    fun register(email: String, password: String) {
        if (email.isBlank() || !email.contains("@")) {
            _registerState.value = "Se necesita un correo válido"
            return
        }
        if (password.length < 6) {
            _registerState.value = "La contraseña debe tener al menos 6 caracteres"
            return
        }

        auth.createUserWithEmailAndPassword(email, password)
            .addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    val userId = auth.currentUser?.uid ?: return@addOnCompleteListener
                    val userMap = hashMapOf(
                        "email" to email,
                        "rol" to "cliente"
                    )
                    db.collection("usuarios").document(userId).set(userMap)
                        .addOnSuccessListener { _registerState.value = "success" }
                        .addOnFailureListener { _registerState.value = it.message }
                } else {
                    _registerState.value = task.exception?.message
                }
            }
    }

    fun saveProfile(onSuccess: () -> Unit, onError: (String?) -> Unit) {
        val userId = auth.currentUser?.uid ?: return onError("Usuario no autenticado")

        val data = hashMapOf(
            "nombre" to nombre.value,
            "apellido" to apellido.value,
            "telefono" to telefono.value,
            "direccion" to direccion.value
        )

        db.collection("usuarios").document(userId)
            .set(data, SetOptions.merge()) // ✅ merge para no borrar "rol"
            .addOnSuccessListener { onSuccess() }
            .addOnFailureListener { onError(it.message) }
    }

    fun isProfileComplete(onResult: (Boolean) -> Unit) {
        val userId = auth.currentUser?.uid
        if (userId == null) {
            onResult(false)
            return
        }

        db.collection("usuarios").document(userId).get()
            .addOnSuccessListener { document ->
                val data = document.data
                val isComplete = data?.get("nombre") != null &&
                        data["apellido"] != null &&
                        data["telefono"] != null &&
                        data["direccion"] != null
                onResult(isComplete)
            }
            .addOnFailureListener {
                onResult(false)
            }
    }

    fun getUserRole(onResult: (String?) -> Unit) {
        val userId = auth.currentUser?.uid
        if (userId == null) {
            onResult(null)
            return
        }

        db.collection("usuarios").document(userId).get()
            .addOnSuccessListener { document ->
                val role = document.getString("rol")
                onResult(role)
            }
            .addOnFailureListener {
                onResult(null)
            }
    }

    fun isUserLoggedIn(): Boolean {
        return auth.currentUser != null
    }

    // ✅ Nueva función: cerrar sesión
    fun logout() {
        auth.signOut()
        _loginState.value = null
    }

    // ✅ Nueva función: cargar los datos del perfil del usuario autenticado
    fun loadUserData() {
        val userId = auth.currentUser?.uid ?: return

        db.collection("usuarios").document(userId).get()
            .addOnSuccessListener { document ->
                _nombre.value = document.getString("nombre") ?: ""
                _apellido.value = document.getString("apellido") ?: ""
                _telefono.value = document.getString("telefono") ?: ""
                _direccion.value = document.getString("direccion") ?: ""
            }
            .addOnFailureListener {
                // Puedes manejar errores si lo deseas
            }
    }
}
