package com.example.boris_compose_tareafinal.ui.theme

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.example.boris_compose_tareafinal.data.RetrofitClient
import com.example.boris_compose_tareafinal.data.UserApi


@Composable
fun ApiScreen(navController: NavController, setMostrarNotificaciones: (Boolean) -> Unit) {
    val usuarios = remember { mutableStateListOf<UserApi>() }

    LaunchedEffect(Unit) {
        try {
            usuarios.addAll(RetrofitClient.apiService.obtenerUsuarios())
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    Column(modifier = Modifier.fillMaxSize().padding(16.dp)) {
        Text("Usuarios de la API", style = MaterialTheme.typography.titleLarge)
        Spacer(modifier = Modifier.height(8.dp))

        if (usuarios.isEmpty()) {
            CircularProgressIndicator()
        } else {
            LazyColumn {
                items(usuarios) { usuario ->
                    Card(
                        modifier = Modifier.fillMaxWidth().padding(8.dp),
                        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
                        elevation = CardDefaults.cardElevation(defaultElevation = 4.dp)
                    ) {
                        Column(modifier = Modifier.padding(16.dp)) {
                            Text(
                                "Nombre: ${usuario.name}",
                                style = MaterialTheme.typography.bodyLarge
                            )
                            Text(
                                "Correo: ${usuario.email}",
                                style = MaterialTheme.typography.bodyMedium
                            )
                        }
                    }
                }
            }
        }

        Spacer(modifier = Modifier.height(16.dp))
        // ✅ Ahora los botones siempre aparecen
        Button(onClick = {
            navController.navigate("home")
            setMostrarNotificaciones(true)
        }) {
            Text("Volver a la pantalla principal")
        }

        Spacer(modifier = Modifier.height(16.dp))

        Button(onClick = {
            navController.popBackStack()
        }) {
            Text("Cerrar Aplicación")
        }


    }
}







