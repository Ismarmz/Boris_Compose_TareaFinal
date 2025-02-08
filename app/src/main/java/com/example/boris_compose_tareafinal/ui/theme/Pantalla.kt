package com.example.boris_compose_tareafinal.ui.theme

sealed class Pantalla(val ruta: String) {
    object Register : Pantalla("register")
    object Main : Pantalla("main")
    object Api : Pantalla("api")
}