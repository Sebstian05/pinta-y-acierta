package com.example.pintaygana

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.unit.dp
import androidx.compose.ui.Alignment
import androidx.navigation.NavController

@Composable
fun StartScreen(navController: NavController) {
    var playerName by remember { mutableStateOf("") }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(text = "Ingrese su nombre para comenzar")

        Spacer(modifier = Modifier.height(16.dp))

        // Campo de texto para ingresar el nombre
        BasicTextField(
            value = playerName,
            onValueChange = { playerName = it },
            modifier = Modifier
                .background(Color.White)
                .padding(8.dp),
            keyboardOptions = KeyboardOptions.Default.copy(imeAction = ImeAction.Done),
            keyboardActions = KeyboardActions(
                onDone = {
                    if (playerName.isNotEmpty()) {
                        navController.navigate("drawing_screen/$playerName")
                    }
                }
            )
        )

        Spacer(modifier = Modifier.height(16.dp))

        // Botón para comenzar a dibujar
        Button(
            onClick = {
                if (playerName.isNotEmpty()) {
                    navController.navigate("drawing_screen/$playerName")
                }
            }
        ) {
            Text(text = "Comenzar a dibujar")
        }
    }
}
