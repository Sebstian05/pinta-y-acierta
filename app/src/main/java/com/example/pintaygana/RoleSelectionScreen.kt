package com.example.pintaygana

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.text.input.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController

@Composable
fun RoleSelectionScreen(navController: NavController, playerName: String, randomId: String) {
    var roomId by remember { mutableStateOf("") }
    var loading by remember { mutableStateOf(false) }
    var errorMessage by remember { mutableStateOf<String?>(null) }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        // Botones de selección de rol
        Row(
            horizontalArrangement = Arrangement.spacedBy(16.dp),
            modifier = Modifier.fillMaxWidth()
        ) {
            Button(onClick = { navController.navigate("drawing_screen/$playerName/guesser/$randomId") }) {
                Text(text = "Soy Adivinador")
            }
            Button(onClick = { navController.navigate("drawing_screen/$playerName/painter/$randomId") }) {
                Text(text = "Soy Pintor")
            }
        }

        Spacer(modifier = Modifier.height(16.dp))

        // Campo de texto para el ID de la sala con placeholder
        Row(verticalAlignment = Alignment.CenterVertically) {
            Box(
                modifier = Modifier
                    .weight(1f)
                    .background(Color.White)
                    .border(1.dp, Color.Black)
                    .padding(8.dp)
            ) {
                if (roomId.isEmpty()) {
                    Text(text = "Ingresa el ID de la sala", color = Color.Gray)
                }
                BasicTextField(
                    value = roomId,
                    onValueChange = { newValue -> roomId = newValue },
                    modifier = Modifier.fillMaxWidth()
                )
            }

            Spacer(modifier = Modifier.width(8.dp))

            // Botón de enviar
            Button(
                onClick = {
                    if (roomId.isNotEmpty()) {
                        loading = true
                        errorMessage = null
                    }
                },
                modifier = Modifier.align(Alignment.CenterVertically)
            ) {
                Text(text = "Enviar")
            }
        }
    }
}

