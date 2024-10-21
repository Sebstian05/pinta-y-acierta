package com.example.pintaygana

import androidx.compose.foundation.layout.*
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController

@Composable
fun RoleSelectionScreen(navController: NavController, playerName: String, randomId: String) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(text = "Hola $playerName, selecciona tu rol")

        Spacer(modifier = Modifier.height(16.dp))

        Button(
            onClick = { navController.navigate("drawing_screen/$playerName/painter/$randomId") }
        ) {
            Text("Soy Pintor")
        }

        Spacer(modifier = Modifier.height(16.dp))

        Button(
            onClick = { navController.navigate("drawing_screen/$playerName/guesser/$randomId") }
        ) {
            Text("Soy Adivinador")
        }
    }
}
