package com.example.boris_compose_tareafinal.data

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "Users")
data class User(
    @PrimaryKey val email: String,
    val nombre: String,
    val contadorAccesos: Int,
    val ultimoAcceso: Long
)