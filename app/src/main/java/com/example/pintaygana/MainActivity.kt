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
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.unit.dp
import androidx.compose.ui.text.input.ImeAction
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

            // Botón para borrar
            Button(
                onClick = {
                    drawingView?.clearCanvas()  // Llama la función de borrado
                },
                modifier = Modifier
                    .padding(16.dp)
                    .align(Alignment.TopStart)
            ) {
                Text("Borrar", color = Color.White)
            }

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
            }

            // Caja de texto y botón de enviar
            Column(
                modifier = Modifier
                    .align(Alignment.BottomCenter)
                    .fillMaxWidth()
                    .padding(16.dp)
            ) {
                BasicTextField(
                    value = textState,
                    onValueChange = { textState = it },
                    modifier = Modifier
                        .fillMaxWidth()
                        .background(Color.White)
                        .padding(8.dp),
                    keyboardOptions = KeyboardOptions.Default.copy(imeAction = ImeAction.Done),
                    keyboardActions = KeyboardActions(
                        onDone = {
                            // Limpia el foco y cierra el teclado cuando se pulsa "Done"
                            focusManager.clearFocus()
                            keyboardController?.hide()
                        }
                    )
                )

                Spacer(modifier = Modifier.height(8.dp))

                Button(
                    onClick = {
                        if (textState.text.isNotEmpty()) {
                            // Procesa el texto (por ejemplo, enviarlo o guardarlo)
                            println("Texto enviado: ${textState.text}")

                            // Limpia el campo de texto después de enviar
                            textState = TextFieldValue("")

                            // Limpia el foco y oculta el teclado
                            focusManager.clearFocus()
                            keyboardController?.hide()
                        }
                    },
                    modifier = Modifier.align(Alignment.End)
                ) {
                    Text("Enviar")
                }
            }

            // Mostrar el nombre del jugador en la parte inferior izquierda debajo del chat
            Text(
                text = "Artista: $playerName",
                modifier = Modifier
                    .align(Alignment.BottomStart)  // Posiciona el nombre en la parte inferior izquierda
                    .padding(16.dp),
                color = Color.Black,
                style = MaterialTheme.typography.titleLarge
            )

            // Efecto desechable para manejar la limpieza del foco al ocultar el teclado
            DisposableEffect(Unit) {
                onDispose {
                    focusManager.clearFocus()
                    keyboardController?.hide()
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
