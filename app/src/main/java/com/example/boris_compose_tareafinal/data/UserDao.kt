package com.example.boris_compose_tareafinal.data

import androidx.room.*

@Dao
interface UserDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertUser(user: User)

    @Query("SELECT * FROM users WHERE email = :email LIMIT 1")
    suspend fun getUser(email: String): User?

    @Update
    suspend fun updateUser(user: User)

    @Query("SELECT * FROM users")
    suspend fun getAllUsers(): List<User>
}
