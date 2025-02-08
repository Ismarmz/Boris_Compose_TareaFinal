package com.example.boris_compose_tareafinal.data
import androidx.room.*
import kotlinx.coroutines.flow.Flow

@Dao
interface UserDAo {
    @Query("SELECT * FROM Users WHERE email = :email")
    fun obtenerUsuario(email: String): Flow<User?>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertarUsuario(usuario: User)

    @Update
    suspend fun actualizarUsuario(usuario: User)

    @Query("SELECT * FROM usuarios")
    suspend fun obtenerTodosUsuarios(): List<User>
}