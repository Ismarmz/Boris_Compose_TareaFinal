package com.example.boris_compose_tareafinal.ui

import android.app.Activity
import android.os.Handler
import android.os.Looper
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import com.example.boris_compose_tareafinal.data.UserViewModel
import com.example.boris_compose_tareafinal.mostrarNotificacion
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

@Composable
fun HomeScreen(navController: NavController, email: String, viewModel: UserViewModel = viewModel()) {
    val user by viewModel.user.collectAsState()
    val context = LocalContext.current
    val handler = remember { Handler(Looper.getMainLooper()) }
    var mostrarNotificaciones by remember { mutableStateOf(true) }

    LaunchedEffect(email) { // ✅ Usamos el email dinámico
        mostrarNotificacion(context, "Bienvenido de nuevo!")
        viewModel.loadUser(email) // ✅ Cargar los datos del usuario correcto
    }

    LaunchedEffect(mostrarNotificaciones) {
        if (mostrarNotificaciones) {
            handler.postDelayed(object : Runnable {
                override fun run() {
                    if (mostrarNotificaciones) {
                        mostrarNotificacion(context, "¡No olvides consultar la API!")
                        handler.postDelayed(this, 500)
                    }
                }
            }, 500)
        }
    }

    user?.let {
        val formattedDate = SimpleDateFormat("dd/MM/yyyy HH:mm", Locale.getDefault()).format(Date(it.lastAccess.toLong()))

        Column(
            modifier = Modifier.fillMaxSize().padding(16.dp),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(text = "Bienvenido, ${it.username}", style = MaterialTheme.typography.titleLarge)
            Spacer(modifier = Modifier.height(8.dp))
            Text(text = "Accesos: ${it.accessCount}")
            Text(text = "Último acceso: $formattedDate")
            Spacer(modifier = Modifier.height(16.dp))

            Button(onClick = {
                navController.navigate("api")
                mostrarNotificaciones = false
            }) {
                Text("Consultar API")
            }

            Spacer(modifier = Modifier.height(16.dp))

            val activity = (context as? Activity)
            Button(onClick = { activity?.finishAffinity() }) {
                Text("Cerrar Aplicación")
            }
        }
    } ?: run {
        Text(text = "Cargando usuario...")
    }
}
