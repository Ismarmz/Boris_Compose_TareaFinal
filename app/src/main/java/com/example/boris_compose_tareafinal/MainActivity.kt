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

        // We create notify channel if is necessary, in android +8
        createNotificationChannel()
        //set content allow us use composable
        setContent {
            //we use this to determinated if we need to show notifies
            var mostrarNotificaciones by remember { mutableStateOf(true) }
            //to use navController
            val navController = rememberNavController()
            //to get app context
            val context = LocalContext.current
            //Use DAO to acces to database
            val userDao = remember { AppDatabase.getInstance(context).usuarioDao() }
            //check if the user is registered
            var usuarioRegistrado by remember { mutableStateOf(false) }

            //execute only 1 time when the app start
            LaunchedEffect(Unit) {
                //Searches the user in the database
                usuarioRegistrado = userDao.getUser("usuarioEjemplo") != null
                println("Usuario registrado: $usuarioRegistrado") // Is to check in the logcat
            }

            //define the navigation of the app
            NavHost(
                navController = navController,
                //if the user is not register stars at register screen, if is register stars at home screen
                startDestination = if (usuarioRegistrado) Pantalla.Home.ruta else Pantalla.Register.ruta
            ) {
                //load register screen
                composable(Pantalla.Register.ruta) {
                    //instance the viewmodel
                    val userViewModel: UserViewModel = viewModel()
                    RegisterScreen(
                        navController,
                        userViewModel
                    )
                }
                //load api screen
                composable(Pantalla.Api.ruta) {
                    ApiScreen(navController) {
                        //recive it to show or not the notifications
                        mostrarNotificaciones = it
                    }
                }
                composable("home/{email}") { backStackEntry ->
                    //load home screan
                    val email = backStackEntry.arguments?.getString("email") ?: ""
                    HomeScreen(navController, email)//we pass the email to lead the user with his data
                }
                //We load the login screen
                composable(Pantalla.Login.ruta) {
                    val userViewModel: UserViewModel = viewModel()
                    LoginScreen(navController, userViewModel)
                }
            }
        }

        //If the sistem is android 13+ ask for the permission to show notifies
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            requestPermissionLauncher.launch(Manifest.permission.POST_NOTIFICATIONS)
        } else {
            //if no, just show the notify
            mostrarNotificacion()
        }
    }

    //We launch to ask for the permission
    @SuppressLint("MissingPermission")
    private val requestPermissionLauncher =
        registerForActivityResult(ActivityResultContracts.RequestPermission()) { isGranted ->
            //If the user accept the permission, we show the notify
            if (isGranted) {
                mostrarNotificacion()
            }
        }

    // We call the notification channel
    private fun createNotificationChannel() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val channelId = "mi_canal_id"
            val channelName = "Canal de Notificaciones"
            val importance = NotificationManager.IMPORTANCE_DEFAULT
            val channel = NotificationChannel(channelId, channelName, importance)
            //we pass al the parameter for the channel

            //register the channel with the system
            val notificationManager = getSystemService(NotificationManager::class.java)
            notificationManager?.createNotificationChannel(channel)
        }
    }

    // We show a notify
    private fun mostrarNotificacion() {
        val channelId = "mi_canal_id"
        val notificationId = 1

        val builder = NotificationCompat.Builder(this, channelId)
            //this is the icon notify
            .setSmallIcon(R.drawable.ic_launcher_foreground)
            .setContentTitle("¡Hola!")
            .setContentText("Esta es una prueba de notificación")
            .setPriority(NotificationCompat.PRIORITY_DEFAULT)

        // Check the permissions and show the notify
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.TIRAMISU ||
            ActivityCompat.checkSelfPermission(this, Manifest.permission.POST_NOTIFICATIONS) == PackageManager.PERMISSION_GRANTED
        ) {
            NotificationManagerCompat.from(this).notify(notificationId, builder.build())
        }
    }
}