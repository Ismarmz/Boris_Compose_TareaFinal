package com.example.boris_compose_tareafinal.ui.theme

sealed class Pantalla(val ruta: String) {
    object Register : Pantalla("register")
    object Api : Pantalla("api")
    object Home : Pantalla("home")
    object Login : Pantalla("login")
}