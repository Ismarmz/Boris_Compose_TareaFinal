package com.example.boris_compose_tareafinal.data

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

//view model subclass, design to be use in android applications and provide context, for example app database
class UserViewModel(application: Application) : AndroidViewModel(application) {
    //obtains the DAO to interact with the database
    private val userDao = AppDatabase.getInstance(application).usuarioDao()
    //data flow to update the UI, in this case null, it means that are not an user load
    //update the user in real time wherever the user appears in this case, in the HomeScreen
    private val _user = MutableStateFlow<User?>(null)
    //access to user, but block modifications in this
    //stateFlow ensures that any change on the users appears in the UI
    val user = _user.asStateFlow()

    //insert a new user with his name and email
    fun insertUser(email: String, username: String) {
        //execute the code in a coroutine to not block the UI
        viewModelScope.launch {
            //trim is to clean blank spaces
            val cleanEmail = email.trim()
            val cleanUsername = username.trim()
            //new user instance
            val newUser = User(
                email = cleanEmail,
                username = cleanUsername,
                accessCount = 1,
                lastAccess = System.currentTimeMillis()
            )
            //this is for check in the logcat
            println("🚀 Insertando usuario: $newUser")
            //insert the new user using room
            userDao.insertUser(newUser)
            //delay to see the animation
            delay(500)

            // ✅ Verificar todos los usuarios en la base de datos
            val allUsers = userDao.getAllUsers()
            println("📋 Usuarios registrados en la base de datos: $allUsers")

            val checkUser = userDao.getUser(cleanEmail)
            println("🔍 Verificando usuario después de inserción: $checkUser")
            //this update and notify to UI the new user
            _user.value = checkUser
        }
    }

    //this is to get an user by email and check if was inserted or not
    suspend fun getUser(email: String): User? {
        return userDao.getUser(email.trim())
    }

    //load and user in the database using his email
    fun loadUser(email: String) {
        //execute the code in a coroutine to not block the UI
        viewModelScope.launch {
            //search the user in the database
            val cleanEmail = email.trim()
            println("🔎 Buscando usuario con email: $cleanEmail")

            val loadedUser = userDao.getUser(cleanEmail)
            if (loadedUser != null) {
                println("✅ Usuario encontrado: $loadedUser")
                //if the user is find it, the funtion load the user in the UI
                _user.value = loadedUser
            } else {
                println("❌ No se encontró el usuario con email: $cleanEmail")
            }
        }
    }

//update the access count and last access of an user
    suspend fun updateAccess(email: String): Boolean {
        //find the user by his email
        val currentUser = userDao.getUser(email)

        return if (currentUser != null) {
            val updatedUser = currentUser.copy(
                //if find the user update his access count and last access
                accessCount = currentUser.accessCount + 1,
                lastAccess = System.currentTimeMillis()
            )
            println("🔄 Actualizando usuario: $updatedUser")
            //update the user in the database
            userDao.updateUser(updatedUser)
            // update the user in the UI
            _user.value = updatedUser
            true // ✅ Usuario encontrado
        } else {
            println("⚠️ Usuario no encontrado, no se puede actualizar acceso.")
            false // ❌ Usuario no encontrado
        }
    }
}
