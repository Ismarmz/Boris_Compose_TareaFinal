package com.example.boris_compose_tareafinal.data

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "users")
data class User(
    @PrimaryKey val email: String, // Cambio de "username" a "email"
    val username: String,          // Mantener el nombre separado si es necesario
    val accessCount: Int = 0,
    val lastAccess: Long = 0L      // Guardar la fecha como Long
)