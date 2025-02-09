
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



    fun insertUser(username: String) {
        viewModelScope.launch {
            val cleanUsername = username.trim() // 🔥 Elimina espacios extra
            val newUser = User(username = cleanUsername, accessCount = 1, lastAccess = System.currentTimeMillis().toString())
            userDao.insertUser(newUser)
            delay(500) // ⏳ Espera a que Room guarde los datos
            val checkUser = userDao.getUser(username) // 🔍 Intenta recuperar el usuario
            println("Verificando usuario después de inserción: $checkUser")
            _user.value = checkUser
            println("Usuario guardado en Room: $newUser") // Ver en Logcat
        }
    }

    suspend fun getUser(email: String): User? {
        return userDao.getUser(email.trim()) // 🔍 Aseguramos que el email no tenga espacios
    }


    fun loadUser(username: String) {
        viewModelScope.launch {
            val usuarios = userDao.getAllUsers() // Obtén todos los usuarios
            println("Usuarios en la BD: $usuarios") // 🔍 Verifica qué usuarios hay en la BD
            val cleanUsername = username.trim()
            println("Buscando usuario con username: $cleanUsername") // 🔍 Ver Logcat

            val loadedUser = userDao.getUser(cleanUsername)
            println("Usuario encontrado en BD: $loadedUser") // 🔍 Ver Logcat

            _user.value = loadedUser
        }
    }






    fun updateAccess(username: String) {
        viewModelScope.launch {
            val currentUser = userDao.getUser(username)
            if (currentUser != null) {
                val updatedUser = currentUser.copy(
                    accessCount = currentUser.accessCount + 1,
                    lastAccess = System.currentTimeMillis().toString()
                )
                userDao.updateUser(updatedUser)
                _user.value = updatedUser
            }
        }
    }






}
