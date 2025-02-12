package com.example.boris_compose_tareafinal.ui.theme

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import com.example.boris_compose_tareafinal.data.UserViewModel
import kotlinx.coroutines.launch

@Composable
//we use navcontroller to navigate to other screens
fun RegisterScreen(navController: NavController, viewModel: UserViewModel = viewModel()) {
    //this varaiables is to save the name and the email, and update the UI every time the user write
    var nombre by remember { mutableStateOf("") }
    var email by remember { mutableStateOf("") }
    //we can use coroutine to check if the user is not register yet, without freezing the UI
    val coroutineScope = rememberCoroutineScope()
    var errorMessage by remember { mutableStateOf("") }

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
                modifier = Modifier
                    .padding(24.dp)
                    .fillMaxWidth(),
                verticalArrangement = Arrangement.spacedBy(16.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Text(
                    text = "Crear Cuenta",
                    style = MaterialTheme.typography.titleLarge.copy(
                        fontWeight = FontWeight.Bold,
                        fontSize = 24.sp
                    )
                )

                //user will enter his name here
                TextField(
                    //current name value
                    value = nombre,
                    //update it everytime the user write it
                    onValueChange = { nombre = it },
                    label = { Text("Nombre Completo") },
                    singleLine = true,
                    modifier = Modifier.fillMaxWidth()
                )
                //user will enter his email here
                TextField(
                    //current email value
                    value = email,
                    //update it everytime the user write it
                    onValueChange = { email = it },
                    label = { Text("Correo Electrónico") },
                    singleLine = true,
                    modifier = Modifier.fillMaxWidth()
                )

                Button(
                    onClick = {
                        //use a coroutine to check if the user is already registered without freezing the UI
                        coroutineScope.launch {
                            //if the email does not exist in the database, the user can register
                            val existingUser = viewModel.getUser(email)
                            if (existingUser == null) {
                                //insert the user and navigate to login screen
                                viewModel.insertUser(email, nombre)
                                navController.navigate("login")
                            } else {
                                //errore message ig the email is already registered
                                errorMessage = "El correo ya está registrado"
                            }
                        }
                    },
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(12.dp)
                ) {
                    Text("Registrarse")
                }

                if (errorMessage.isNotEmpty()) {
                    Text(
                        errorMessage,
                        color = MaterialTheme.colorScheme.error,
                        style = MaterialTheme.typography.bodyMedium
                    )
                }

                HorizontalDivider(modifier = Modifier.padding(vertical = 8.dp))

                TextButton(
                    //if the user already have a account he can navigate to login screen
                    onClick = { navController.navigate(Pantalla.Login.ruta) }
                ) {
                    Text("¿Ya tienes cuenta? Inicia sesión", color = MaterialTheme.colorScheme.primary)
                }
            }
        }
    }
}
