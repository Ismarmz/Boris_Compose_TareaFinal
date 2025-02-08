package com.example.boris_compose_tareafinal.ui.theme

import android.content.Context
import android.widget.Toast
import androidx.compose.foundation.layout.*
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import kotlinx.coroutines.launch
import com.example.boris_compose_tareafinal.data.User
import com.example.boris_compose_tareafinal.data.AppDatabase

@Composable
fun RegisterScreen(navController: NavController, context: Context) {
    var nombre by remember { mutableStateOf("") }
    var email by remember { mutableStateOf("") }
    val coroutineScope = rememberCoroutineScope()
    val db = remember { AppDatabase.getInstance(context).usuarioDao() }

    Column(
        modifier = Modifier.fillMaxSize().padding(16.dp),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        TextField(
            value = nombre,
            onValueChange = { nombre = it },
            label = { Text("Nombre") },
            modifier = Modifier.fillMaxWidth()
        )

        Spacer(modifier = Modifier.height(8.dp))

        TextField(
            value = email,
            onValueChange = { email = it },
            label = { Text("Correo Electrónico") },
            modifier = Modifier.fillMaxWidth()
        )

        Spacer(modifier = Modifier.height(16.dp))

        Button(
            onClick = {
                if (nombre.isNotBlank() && email.isNotBlank()) {
                    coroutineScope.launch {
                        val usuario = User(email, nombre, 1, System.currentTimeMillis())
                        db.insertarUsuario(usuario)

                        Toast.makeText(context, "Usuario registrado", Toast.LENGTH_SHORT).show()
                        navController.navigate(Pantalla.Main.ruta)
                    }
                } else {
                    Toast.makeText(context, "Por favor, llena todos los campos", Toast.LENGTH_SHORT).show()
                }
            },
            modifier = Modifier.fillMaxWidth()
        ) {
            Text("Registrar")
        }
    }
}