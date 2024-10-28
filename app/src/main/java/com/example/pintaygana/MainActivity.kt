package com.example.pintaygana

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.Alignment
import androidx.compose.ui.draw.clip
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.unit.dp
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.viewinterop.AndroidView
import androidx.navigation.compose.NavHost
import java.net.URI
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import java.util.UUID

class MainActivity : ComponentActivity() {

    private lateinit var webSocketClient: DrawingWebSocketClient

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        // Configura la conexión WebSocket
        val serverUri = URI("ws://echo.websocket.org") // Cambia la URL por tu servidor WebSocket si es necesario
        webSocketClient = DrawingWebSocketClient(serverUri)
        webSocketClient.connect()

        // Usa Jetpack Compose para el contenido de la actividad
        setContent {
            val navController = rememberNavController()
            val randomId = UUID.randomUUID().toString() // Generar ID aleatorio

            NavHost(navController = navController, startDestination = "start_screen") {
                // Pantalla de inicio para ingresar el nombre
                composable("start_screen") {
                    StartScreen(navController)
                }

                composable("role_selection_screen/{playerName}") { backStackEntry ->
                    val playerName = backStackEntry.arguments?.getString("playerName") ?: "Unknown"
                    RoleSelectionScreen(navController, playerName, randomId) // Pasar ID aleatorio
                }

                composable("drawing_screen/{playerName}/{role}/{randomId}") { backStackEntry ->
                    val playerName = backStackEntry.arguments?.getString("playerName") ?: "Unknown"
                    val role = backStackEntry.arguments?.getString("role") ?: "guesser"
                    val id = backStackEntry.arguments?.getString("randomId") ?: "Unknown"

                    // Crea la instancia de DrawingView
                    val context = LocalContext.current
                    val drawingView = remember { DrawingView(context, null) }

                    // Pasa los parámetros playerName, role y el drawingView a DrawingScreen
                    DrawingScreen(playerName, role, drawingView)
                }
            }
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        // Cierra la conexión cuando la actividad sea destruida
        webSocketClient.close()
    }
}


// Pantalla de dibujo modificada para recibir y mostrar el nombre del jugador
@Composable
fun DrawingScreen(playerName: String) {
    // Controlador del teclado y gestor de enfoque
    val keyboardController = LocalSoftwareKeyboardController.current
    val focusManager = LocalFocusManager.current

    // Variable de estado para el color seleccionado
    var selectedColor by remember { mutableStateOf(android.graphics.Color.BLACK) }
    var drawingView: DrawingView? = null

    // Variable de estado para el texto en la caja de chat
    var textState by remember { mutableStateOf(TextFieldValue("")) }

    // Caja que detecta toques para ocultar el teclado
    Box(
        modifier = Modifier
            .fillMaxSize()
            .pointerInput(Unit) {
                detectTapGestures(onTap = {
                    focusManager.clearFocus() // Limpia el foco para ocultar el teclado
                })
            }
    ) {
        // Vista de Dibujo (DrawingView)
        AndroidView(
            factory = { context ->
                DrawingView(context, null).also { view ->
                    drawingView = view
                    view.setColor(selectedColor)
                }
            },
            modifier = Modifier.fillMaxSize()
        )

        // Fila con botones de colores
        Row(
            modifier = Modifier
                .padding(16.dp)
                .align(Alignment.TopEnd)
        ) {
            colorButton(Color.Yellow, android.graphics.Color.YELLOW) {
                selectedColor = it
                drawingView?.setColor(selectedColor)
            }

            Spacer(modifier = Modifier.width(8.dp))

            colorButton(Color.Blue, android.graphics.Color.BLUE) {
                selectedColor = it
                drawingView?.setColor(selectedColor)
            }

            Spacer(modifier = Modifier.width(8.dp))

            colorButton(Color.Red, android.graphics.Color.RED) {
                selectedColor = it
                drawingView?.setColor(selectedColor)
            }

            Spacer(modifier = Modifier.width(8.dp))

            colorButton(Color(0xFF8033FF), android.graphics.Color.rgb(128, 51, 255)) {
                selectedColor = it
                drawingView?.setColor(selectedColor)
            }

            Spacer(modifier = Modifier.width(8.dp))

            colorButton(Color.Green, android.graphics.Color.GREEN) {
                selectedColor = it
                drawingView?.setColor(selectedColor)
            }

            Spacer(modifier = Modifier.width(8.dp))

            colorButton(Color(0xFFFFA500), android.graphics.Color.rgb(255, 165, 0)) {
                selectedColor = it
                drawingView?.setColor(selectedColor)
            }

            Button(
                onClick = {
                    drawingView?.clearCanvas() // Llama a clearCanvas solo si drawingView no es nulo
                },
                modifier = Modifier
                    .size(50.dp)
                    .clip(CircleShape)
                    .background(Color.White),
                colors = ButtonDefaults.buttonColors(containerColor = Color.Transparent),
                contentPadding = PaddingValues(0.dp)
            ) {
                Text(text = "Borrar", color = Color.Black)
            }
        }

    }
}

@Composable
fun colorButton(backgroundColor: Color, androidColor: Int, onClick: (Int) -> Unit) {
    Button(
        onClick = { onClick(androidColor) },
        modifier = Modifier
            .size(50.dp)
            .clip(CircleShape)
            .background(backgroundColor),
        colors = ButtonDefaults.buttonColors(containerColor = Color.Transparent),
        contentPadding = PaddingValues(0.dp)
    ) {}
}