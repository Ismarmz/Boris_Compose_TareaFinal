package com.example.boris_compose_tareafinal.data

import retrofit2.http.GET

//this data class get a list of users from an api rest
data class UserApi(
    //those are all the fields of the json
    val id: Int,
    val name: String,
    val email: String
)

//we make request to HTTP with this interface
interface ApiService {
    //we make the request to users path in the api rest
    @GET("users")
    //return an users list from the api
    suspend fun obtenerUsuarios(): List<UserApi>
}