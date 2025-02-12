package com.example.boris_compose_tareafinal.data

import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

//this class create a retrofit client to do request to the api wherever we want in our app without repeat code
//this is a singleton object
object RetrofitClient {
    //we build a retrofit instance
    private val retrofit = Retrofit.Builder()
        // URL api
        .baseUrl("https://jsonplaceholder.typicode.com/") // must ends in '/' to add all the paths
        .addConverterFactory(GsonConverterFactory.create())// Convert all Gson data to kotlin objects
        .build() //we build retrofit object
    //we use our ApiService interface here to make the request
    val apiService: ApiService = retrofit.create(ApiService::class.java)
}