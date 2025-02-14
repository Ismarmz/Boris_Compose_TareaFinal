package com.example.boris_compose_tareafinal.data

import androidx.room.*

//this tag is because this is our DAO (Data access object) so this is where we define our queries
@Dao

//We use as interface to use the queries in the app
interface UserDao {
    //insert an user, and if this already exists, replace it
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertUser(user: User)

    //search an user by email, is limit to return only 1 email
    @Query("SELECT * FROM users WHERE email = :email LIMIT 1")
    suspend fun getUser(email: String): User?

    //update an user, but this must exist
    @Update
    suspend fun updateUser(user: User)

    //get all users in the table
    @Query("SELECT * FROM users")
    suspend fun getAllUsers(): List<User>
}
