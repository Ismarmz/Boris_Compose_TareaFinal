package com.example.boris_compose_tareafinal.data

import androidx.room.Entity
import androidx.room.PrimaryKey

//Entity of the user, is for use it with DAO (Data access object), and let us work in the table users with queries
@Entity(tableName = "users")
data class User(
    //we use the email as primary key, because a lot users can use the same name but not the
    @PrimaryKey val email: String,
    val username: String,
    val accessCount: Int = 0, //number of times the user has accessed the app
    val lastAccess: Long = 0L //we use long because is useful to save timestamps
)