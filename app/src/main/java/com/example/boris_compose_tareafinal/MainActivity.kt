package com.example.boris_compose_tareafinal

import android.Manifest
import android.annotation.SuppressLint
import android.app.NotificationChannel
import android.app.NotificationManager
import android.content.pm.PackageManager
import android.os.Build
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.platform.LocalContext
import androidx.core.app.ActivityCompat
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.boris_compose_tareafinal.data.AppDatabase
import com.example.boris_compose_tareafinal.data.UserViewModel
import com.example.boris_compose_tareafinal.ui.HomeScreen
import com.example.boris_compose_tareafinal.ui.theme.ApiScreen
import com.example.boris_compose_tareafinal.ui.theme.LoginScreen
import com.example.boris_compose_tareafinal.ui.theme.Pantalla
import com.example.boris_compose_tareafinal.ui.theme.RegisterScreen

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        // Crear el canal de notificación si es necesario
        createNotificationChannel()
        setContent {
            var mostrarNotificaciones by remember { mutableStateOf(true) }
            val navController = rememberNavController()
            val context = LocalContext.current
            val userDao = remember { AppDatabase.getInstance(context).usuarioDao() }
            var usuarioRegistrado by remember { mutableStateOf(false) }

            LaunchedEffect(Unit) {
                usuarioRegistrado = userDao.getUser("usuarioEjemplo") != null
                println("Usuario registrado: $usuarioRegistrado") // 👈 Ver esto en Logcat
            }

            NavHost(
                navController = navController,
                startDestination = if (usuarioRegistrado) Pantalla.Home.ruta else Pantalla.Register.ruta
            ) {
                composable(Pantalla.Register.ruta) {
                    val userViewModel: UserViewModel = viewModel()
                    RegisterScreen(
                        navController,
                        userViewModel
                    ) // ✅ Corrección: Se eliminó `context`
                }
                composable(Pantalla.Api.ruta) {
                    ApiScreen(navController) {
                        mostrarNotificaciones = it
                    }
                }
                composable("home/{email}") { backStackEntry ->
                    val email = backStackEntry.arguments?.getString("email") ?: ""
                    HomeScreen(navController, email) // ✅ Pasamos el email a HomeScreen
                }
                composable(Pantalla.Login.ruta) { // ✅ Aseguramos que la ruta existe
                    val userViewModel: UserViewModel = viewModel()
                    LoginScreen(navController, userViewModel)
                }
            }
        }

        // Pedir permisos de notificación en Android 13+
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            requestPermissionLauncher.launch(Manifest.permission.POST_NOTIFICATIONS)
        } else {
            mostrarNotificacion()
        }
    }

    // Launcher para pedir permiso de notificación en Android 13+
    @SuppressLint("MissingPermission")
    private val requestPermissionLauncher =
        registerForActivityResult(ActivityResultContracts.RequestPermission()) { isGranted ->
            if (isGranted) {
                mostrarNotificacion()
            }
        }

    // Crear canal de notificación (Android 8+)
    private fun createNotificationChannel() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val channelId = "mi_canal_id"
            val channelName = "Canal de Notificaciones"
            val importance = NotificationManager.IMPORTANCE_DEFAULT
            val channel = NotificationChannel(channelId, channelName, importance)

            val notificationManager = getSystemService(NotificationManager::class.java)
            notificationManager?.createNotificationChannel(channel)
        }
    }

    // Función para mostrar una notificación
    private fun mostrarNotificacion() {
        val channelId = "mi_canal_id"
        val notificationId = 1

        val builder = NotificationCompat.Builder(this, channelId)
            .setSmallIcon(R.drawable.ic_launcher_foreground) // Asegúrate de que este ícono existe
            .setContentTitle("¡Hola!")
            .setContentText("Esta es una prueba de notificación")
            .setPriority(NotificationCompat.PRIORITY_DEFAULT)

        // Verificar permisos antes de mostrar la notificación
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.TIRAMISU ||
            ActivityCompat.checkSelfPermission(this, Manifest.permission.POST_NOTIFICATIONS) == PackageManager.PERMISSION_GRANTED
        ) {
            NotificationManagerCompat.from(this).notify(notificationId, builder.build())
        }
    }
}
