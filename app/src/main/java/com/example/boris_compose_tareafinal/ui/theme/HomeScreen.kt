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
//we use navcontroller to navigate to other screens
//the email to access to the user data
//we use the viewModel to use the users data
fun HomeScreen(navController: NavController, email: String, viewModel: UserViewModel = viewModel()) {
    //check the user state in real time from the UserViewModel
    val user by viewModel.user.collectAsState()
    //get the current context of the app
    val context = LocalContext.current
    //handler to show periodic notifications
    val handler = remember { Handler(Looper.getMainLooper()) }
    //control the notifications
    var mostrarNotificaciones by remember { mutableStateOf(true) }

    //Excute when HomeScreen Shows and load the user data
    LaunchedEffect(email) {
        viewModel.loadUser(email)
    }


    DisposableEffect(Unit) {
        onDispose {
            handler.removeCallbacksAndMessages(null) // stops notifications whe leave the app
        }
    }

    //shows the periodic notifications
    LaunchedEffect(mostrarNotificaciones) {
        //if show the notifies is true
        if (mostrarNotificaciones) {
            handler.postDelayed(object : Runnable {
                override fun run() {
                    if (mostrarNotificaciones) {
                        mostrarNotificacion(context, "¡No olvides consultar la API!")
                        handler.postDelayed(this, 1500) // Show notification every 1,5 seconds
                    }
                }
            }, 1500)
        }
    }

    //if user is not null
    if (user != null) {
        //Get the date and formated it to show a more understandable date for the user
        val formattedDate = SimpleDateFormat("dd/MM/yyyy HH:mm", Locale.getDefault()).format(Date(user!!.lastAccess))

        //aling the box in to the center of the screen
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
                        //gets the name to show it
                        text = "¡Bienvenido, ${user!!.username}!",
                        style = MaterialTheme.typography.titleLarge.copy(
                            fontWeight = FontWeight.Bold,
                            fontSize = 26.sp
                        )
                    )

                    //shows the user access and last access with formated date
                    Text("Accesos: ${user!!.accessCount}", style = MaterialTheme.typography.bodyLarge)
                    Text("Último acceso: $formattedDate", style = MaterialTheme.typography.bodyMedium)

                    Spacer(modifier = Modifier.height(16.dp))

                    Button(
                        onClick = {
                            //navController to navigate to api screen and stop notifies when the user touch
                            //in to the button
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
        //if can load an user shows a circle to show that is loading
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


