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
import androidx.compose.ui.tooling.preview.Preview
import com.example.pintaygana.ui.theme.PintaYGanaTheme
import android.view.View
import androidx.compose.ui.viewinterop.AndroidView
import androidx.compose.ui.unit.dp
import androidx.compose.ui.Alignment
import androidx.compose.ui.draw.clip



class MainActivity : ComponentActivity() {
        override fun onCreate(savedInstanceState: Bundle?) {
            super.onCreate(savedInstanceState)

            setContent {
                PintaYGanaTheme {
                    // Definimos un estado para el color de dibujo
                    var selectedColor by remember { mutableStateOf(android.graphics.Color.BLACK) }

                    // Inicializa la referencia a drawingView como mutable
                    var drawingView: DrawingView? = null

                    Box(modifier = Modifier.fillMaxSize()) {
                        // Vista de Dibujo (DrawingView)
                        AndroidView(
                            factory = { context ->
                                DrawingView(context, null).also { view ->
                                    drawingView = view
                                    // Establece el color inicial después de crear el DrawingView
                                    view.setColor(selectedColor)
                                }
                            },
                            modifier = Modifier
                                .fillMaxSize()
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
                            Text(
                                "Borrar",
                                color = Color.White
                            )
                        }

                        // Fila con los 3 botones circulares de colores en la esquina superior derecha
                        Row(
                            modifier = Modifier
                                .padding(16.dp)
                                .align(Alignment.TopEnd)
                        ) {
                            // Botón Amarillo
                            Button(
                                onClick = {
                                    selectedColor = android.graphics.Color.YELLOW
                                    drawingView?.setColor(selectedColor) // Cambia el color a amarillo
                                },
                                modifier = Modifier
                                    .size(50.dp)  // Tamaño circular
                                    .clip(CircleShape)
                                    .background(Color.Yellow),  // Fondo amarillo
                                colors = ButtonDefaults.buttonColors(containerColor = Color.Transparent), // Para evitar efectos adicionales
                                contentPadding = PaddingValues(0.dp) // Sin padding adicional
                            ) {}

                            Spacer(modifier = Modifier.width(8.dp)) // Espacio entre los botones

                            // Botón Azul
                            Button(
                                onClick = {
                                    selectedColor = android.graphics.Color.BLUE
                                    drawingView?.setColor(selectedColor) // Cambia el color a azul
                                },
                                modifier = Modifier
                                    .size(50.dp)
                                    .clip(CircleShape)
                                    .background(Color.Blue),  // Fondo azul
                                colors = ButtonDefaults.buttonColors(containerColor = Color.Transparent),
                                contentPadding = PaddingValues(0.dp)
                            ) {}

                            Spacer(modifier = Modifier.width(8.dp))

                            // Botón Rojo
                            Button(
                                onClick = {
                                    selectedColor = android.graphics.Color.RED
                                    drawingView?.setColor(selectedColor) // Cambia el color a rojo
                                },
                                modifier = Modifier
                                    .size(50.dp)
                                    .clip(CircleShape)
                                    .background(Color.Red),  // Fondo rojo
                                colors = ButtonDefaults.buttonColors(containerColor = Color.Transparent),
                                contentPadding = PaddingValues(0.dp)
                            ) {}

                            Spacer(modifier = Modifier.width(8.dp))

                            Button(
                                onClick = {
                                    selectedColor = android.graphics.Color.rgb(128, 51, 255)
                                    drawingView?.setColor(selectedColor) // Cambia el color a VIOLETA
                                },
                                modifier = Modifier
                                    .size(50.dp)
                                    .clip(CircleShape)
                                    .background(Color(0xFF8033FF)),  // Fondo rojo
                                colors = ButtonDefaults.buttonColors(containerColor = Color.Transparent),
                                contentPadding = PaddingValues(0.dp)
                            ) {}

                            Spacer(modifier = Modifier.width(8.dp))

                            Button(
                                onClick = {
                                    selectedColor = android.graphics.Color.GREEN
                                    drawingView?.setColor(selectedColor) // Cambia el color a verde
                                },
                                modifier = Modifier
                                    .size(50.dp)
                                    .clip(CircleShape)
                                    .background(Color.Green),  // Fondo rojo
                                colors = ButtonDefaults.buttonColors(containerColor = Color.Transparent),
                                contentPadding = PaddingValues(0.dp)
                            ) {}

                            Spacer(modifier = Modifier.width(8.dp))

                            Button(
                                onClick = {
                                    selectedColor = android.graphics.Color.rgb(255, 165, 0)
                                    drawingView?.setColor(selectedColor) // Cambia el color a naranja
                                },
                                modifier = Modifier
                                    .size(50.dp)
                                    .clip(CircleShape)
                                    .background(Color(0xFFFFA500)),  // Fondo rojo
                                colors = ButtonDefaults.buttonColors(containerColor = Color.Transparent),
                                contentPadding = PaddingValues(0.dp)
                            ) {}
                    }
                }
            }
        }
    }
}









