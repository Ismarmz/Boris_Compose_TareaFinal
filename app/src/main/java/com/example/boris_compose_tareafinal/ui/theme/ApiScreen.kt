package com.example.boris_compose_tareafinal.ui.theme

import android.app.Activity
import android.content.Context
import android.content.ContextWrapper
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.example.boris_compose_tareafinal.data.RetrofitClient
import com.example.boris_compose_tareafinal.data.UserApi

// ✅ Extensión para obtener la actividad de forma segura
fun Context.findActivity(): Activity? = when (this) {
    is Activity -> this
    is ContextWrapper -> baseContext.findActivity()
    else -> null
}

@Composable
fun ApiScreen(navController: NavController, setMostrarNotificaciones: (Boolean) -> Unit) {
    val usuarios = remember { mutableStateListOf<UserApi>() }
    val activity = LocalContext.current.findActivity() // ✅ Forma segura de obtener la actividad

    LaunchedEffect(Unit) {
        try {
            usuarios.addAll(RetrofitClient.apiService.obtenerUsuarios())
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    Column(modifier = Modifier
        .fillMaxSize()
        .padding(16.dp)) {

        Text(
            "Usuarios de la API",
            style = MaterialTheme.typography.titleLarge.copy(
                fontWeight = FontWeight.Bold,
                fontSize = 24.sp
            )
        )

        Spacer(modifier = Modifier.height(16.dp))

        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Button(
                onClick = {
                    navController.popBackStack()
                    setMostrarNotificaciones(true)
                },
                shape = RoundedCornerShape(12.dp)
            ) {
                Text("Volver a la pantalla principal")
            }

            Button(
                onClick = {
                    activity?.finishAffinity()
                },
                shape = RoundedCornerShape(12.dp)
            ) {
                Text("Cerrar Aplicación")
            }
        }

        Spacer(modifier = Modifier.height(16.dp))

        if (usuarios.isEmpty()) {
            Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                CircularProgressIndicator()
            }
        } else {
            LazyColumn(modifier = Modifier.weight(1f)) {
                items(usuarios) { usuario ->
                    Card(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(8.dp),
                        shape = RoundedCornerShape(12.dp),
                        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
                        elevation = CardDefaults.cardElevation(defaultElevation = 4.dp)
                    ) {
                        Column(modifier = Modifier.padding(16.dp)) {
                            Text(
                                "Nombre: ${usuario.name}",
                                style = MaterialTheme.typography.bodyLarge.copy(fontWeight = FontWeight.Medium)
                            )
                            Text(
                                "Correo: ${usuario.email}",
                                style = MaterialTheme.typography.bodyMedium.copy(color = MaterialTheme.colorScheme.primary)
                            )
                        }
                    }
                }
            }
        }
    }
}


