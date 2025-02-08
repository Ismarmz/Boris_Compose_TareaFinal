package com.example.boris_compose_tareafinal.ui.theme

import android.content.Context
import android.os.Handler
import android.os.Looper
import androidx.compose.foundation.layout.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import android.app.NotificationChannel
import android.app.NotificationManager
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.ui.platform.LocalContext
import com.example.boris_compose_tareafinal.mostrarNotificacion

@Composable
fun MainScreen(navController: NavController, context: Context) {
    val context = LocalContext.current
    val handler = remember { Handler(Looper.getMainLooper()) }
    var mostrarNotificaciones by remember { mutableStateOf(true) }

    // Crear canal de notificación (solo necesario en Android 8+)
    LaunchedEffect(Unit) {
        val channel = NotificationChannel(
            "canal_notificaciones",
            "Notificaciones",
            NotificationManager.IMPORTANCE_HIGH
        )
        val manager = context.getSystemService(NotificationManager::class.java)
        manager.createNotificationChannel(channel)

        // Enviar notificación al iniciar la pantalla
        mostrarNotificacion(context, "Bienvenido a la aplicación")
    }

    Column(
        modifier = Modifier.fillMaxSize().padding(16.dp),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Button(onClick = {
            navController.navigate(Pantalla.Api.ruta)
            mostrarNotificaciones = false // Detiene las notificaciones cuando consulta la API
        }) {
            Text("Consultar API")
        }

        Spacer(modifier = Modifier.height(16.dp))

        Button(onClick = { System.exit(0) }) {
            Text("Cerrar Aplicación")
        }
    }

    // Hilo Runnable para mostrar notificaciones cada 500ms hasta que consulte la API
    LaunchedEffect(mostrarNotificaciones) {
        if (mostrarNotificaciones) {
            handler.postDelayed(object : Runnable {
                override fun run() {
                    mostrarNotificacion(context, "¡No olvides consultar la API!")
                    if (mostrarNotificaciones) {
                        handler.postDelayed(this, 500)
                    }
                }
            }, 500)
        }
    }
}