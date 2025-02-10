package com.example.boris_compose_tareafinal.ui

import android.os.Handler
import android.os.Looper
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
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

    LaunchedEffect(email) {
        viewModel.loadUser(email)
    }

    DisposableEffect(Unit) {
        onDispose {
            handler.removeCallbacksAndMessages(null) // Detener notificaciones al salir
        }
    }

    LaunchedEffect(mostrarNotificaciones) {
        if (mostrarNotificaciones) {
            handler.postDelayed(object : Runnable {
                override fun run() {
                    if (mostrarNotificaciones) {
                        mostrarNotificacion(context, "¡No olvides consultar la API!")
                        handler.postDelayed(this, 1500) // ⏱️ Aumentado el tiempo de notificación
                    }
                }
            }, 1500)
        }
    }

    if (user != null) {
        val formattedDate = SimpleDateFormat("dd/MM/yyyy HH:mm", Locale.getDefault()).format(Date(user!!.lastAccess))

        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(MaterialTheme.colorScheme.background)
                .padding(24.dp),
            contentAlignment = Alignment.Center
        ) {
            Card(
                shape = RoundedCornerShape(16.dp),
                elevation = CardDefaults.cardElevation(8.dp),
                modifier = Modifier.fillMaxWidth()
            ) {
                Column(
                    modifier = Modifier.padding(24.dp),
                    verticalArrangement = Arrangement.spacedBy(12.dp),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Text(
                        text = "¡Bienvenido, ${user!!.username}!",
                        style = MaterialTheme.typography.titleLarge.copy(
                            fontWeight = FontWeight.Bold,
                            fontSize = 26.sp
                        )
                    )

                    Text("Accesos: ${user!!.accessCount}", style = MaterialTheme.typography.bodyLarge)
                    Text("Último acceso: $formattedDate", style = MaterialTheme.typography.bodyMedium)

                    Spacer(modifier = Modifier.height(16.dp))

                    Button(
                        onClick = {
                            navController.navigate("api")
                            mostrarNotificaciones = false
                        },
                        shape = RoundedCornerShape(12.dp),
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Text("Consultar API")
                    }
                }
            }
        }
    } else {
        Column(
            modifier = Modifier.fillMaxSize(),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            CircularProgressIndicator()
            Spacer(modifier = Modifier.height(8.dp))
            Text("Cargando usuario...")
        }
    }
}


