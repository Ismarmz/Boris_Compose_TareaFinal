package com.example.boris_compose_tareafinal.data

import retrofit2.http.GET

data class UserApi(
    val id: Int,
    val name: String,
    val email: String
)

interface ApiService {
    @GET("users")
    suspend fun obtenerUsuarios(): List<UserApi>

}