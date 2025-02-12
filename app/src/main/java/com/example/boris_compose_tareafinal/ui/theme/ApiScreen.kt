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

//we use this to get the current activity
fun Context.findActivity(): Activity? = when (this) {
    //if the context is an activity return it
    is Activity -> this
    //if is an context wrapper still searching the activity
    is ContextWrapper -> baseContext.findActivity()
    else -> null
}
@Composable
//we declare navcontroller to navigate to other screens
//boolean to show the notifies
fun ApiScreen(navController: NavController, setMostrarNotificaciones: (Boolean) -> Unit) {
    //we create a mutable list to save the users from the api
    val usuarios = remember { mutableStateListOf<UserApi>() }
    //variable to get the activity, and close the activity in the future
    val activity = LocalContext.current.findActivity()
    //excute the code only 1 time when the screen is loaded
    LaunchedEffect(Unit) {
        try {
            //call retrofit to get the users from the api
            usuarios.addAll(RetrofitClient.apiService.obtenerUsuarios())
            //we control the exceptions
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    Column(modifier = Modifier
        .fillMaxSize()
        .padding(16.dp)) {

        //title of the screen
        Text(
            "Usuarios de la API",
            style = MaterialTheme.typography.titleLarge.copy(
                fontWeight = FontWeight.Bold,
                fontSize = 24.sp
            )
        )
        Spacer(modifier = Modifier.height(16.dp))
        //organize the buttons on horizontal
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Button(
                onClick = {
                    //when we touch the button this navigate to last activity
                    navController.popBackStack()
                    //reactivate the notifications
                    setMostrarNotificaciones(true)
                },
                shape = RoundedCornerShape(12.dp)
            ) {
                Text("Volver a la pantalla principal")
            }

            Button(
                onClick = {
                    //close the app
                    activity?.finishAffinity()
                },
                shape = RoundedCornerShape(12.dp)
            ) {
                Text("Cerrar Aplicación")
            }
        }
        Spacer(modifier = Modifier.height(16.dp))

        if (usuarios.isEmpty()) {
            //if fail to connect to api, shows a load circle
            Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                CircularProgressIndicator()
            }
        } else {
            //optimize column to show the users
            LazyColumn(modifier = Modifier.weight(1f)) {
                //go to api users list
                items(usuarios) { usuario ->
                    //show all the users in a card
                    Card(
                        modifier = Modifier
                            //use all the space
                            .fillMaxWidth()
                            //8dp margin
                            .padding(8.dp),
                        shape = RoundedCornerShape(12.dp),
                        //back card color
                        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
                        //shadow effect
                        elevation = CardDefaults.cardElevation(defaultElevation = 4.dp)
                    ) {
                        //show the api user name and email
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


