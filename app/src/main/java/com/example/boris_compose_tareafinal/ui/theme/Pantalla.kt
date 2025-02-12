package com.example.boris_compose_tareafinal.ui.theme


//we created a sealed class to avoid others class unexpected inherit from this class
sealed class Pantalla(val ruta: String) {
    //this objects can instance a lot times in another classes to navigate to other screens
    //the ruta is the name of the screen that we will use as parameter
    object Register : Pantalla("register")
    object Api : Pantalla("api")
    object Home : Pantalla("home")
    object Login : Pantalla("login")
}