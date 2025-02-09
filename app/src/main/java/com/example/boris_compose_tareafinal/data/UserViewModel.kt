package com.example.boris_compose_tareafinal.data

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class UserViewModel(application: Application) : AndroidViewModel(application) {
    private val userDao = AppDatabase.getInstance(application).usuarioDao()
    private val _user = MutableStateFlow<User?>(null)
    val user = _user.asStateFlow()

    fun insertUser(email: String, username: String) {
        viewModelScope.launch {
            val cleanEmail = email.trim()
            val cleanUsername = username.trim()
            val newUser = User(
                email = cleanEmail,
                username = cleanUsername,
                accessCount = 1,
                lastAccess = System.currentTimeMillis()
            )
            println("🚀 Insertando usuario: $newUser")
            userDao.insertUser(newUser)
            delay(500)

            // ✅ Verificar todos los usuarios en la base de datos
            val allUsers = userDao.getAllUsers()
            println("📋 Usuarios registrados en la base de datos: $allUsers")

            val checkUser = userDao.getUser(cleanEmail)
            println("🔍 Verificando usuario después de inserción: $checkUser")
            _user.value = checkUser
        }
    }
    suspend fun getUser(email: String): User? {
        return userDao.getUser(email.trim()) // 🔍 Aseguramos que el email no tenga espacios
    }
    fun loadUser(email: String) {
        viewModelScope.launch {
            val cleanEmail = email.trim()
            println("🔎 Buscando usuario con email: $cleanEmail")
            val loadedUser = userDao.getUser(cleanEmail)
            if (loadedUser != null) {
                println("✅ Usuario encontrado: $loadedUser")
                _user.value = loadedUser
            } else {
                println("❌ No se encontró el usuario con email: $cleanEmail")
            }
        }
    }
    suspend fun updateAccess(email: String): Boolean {
        val currentUser = userDao.getUser(email)
        return if (currentUser != null) {
            val updatedUser = currentUser.copy(
                accessCount = currentUser.accessCount + 1,
                lastAccess = System.currentTimeMillis()
            )
            println("🔄 Actualizando usuario: $updatedUser")
            userDao.updateUser(updatedUser)
            _user.value = updatedUser
            true // ✅ Usuario encontrado
        } else {
            println("⚠️ Usuario no encontrado, no se puede actualizar acceso.")
            false // ❌ Usuario no encontrado
        }
    }
}
