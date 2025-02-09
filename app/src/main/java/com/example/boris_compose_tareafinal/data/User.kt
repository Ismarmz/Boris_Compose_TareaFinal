package com.example.boris_compose_tareafinal.data

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "users")
data class User(
    @PrimaryKey val username: String,
    val accessCount: Int = 0,
    val lastAccess: String = ""
)