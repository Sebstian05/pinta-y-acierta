package com.example.pintaygana

import android.content.Context
import android.graphics.*
import android.util.AttributeSet
import android.view.MotionEvent
import androidx.compose.ui.unit.dp
import android.view.View
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.viewinterop.AndroidView
import androidx.core.content.ContextCompat
import kotlin.random.Random

@Composable
fun ObjectSelectionScreen(onObjectSelected: (String) -> Unit) {
    val objects = listOf("Carro", "Perro", "Avión")

    AlertDialog(
        onDismissRequest = {},
        title = { Text("Selecciona un objeto para dibujar") },
        text = {
            Column {
                objects.forEach { obj ->
                    Button(onClick = { onObjectSelected(obj) }) {
                        Text(obj)
                    }
                }
            }
        },
        confirmButton = {
            Button(onClick = { /* Cierra el diálogo */ }) {
                Text("Cancelar")
            }
        }
    )
}

@Composable
fun DrawingScreen(playerName: String, role: String, drawingView: DrawingView) {
    val isPainter = role == "painter"
    var textState by remember { mutableStateOf("") }
    var selectedObject by remember { mutableStateOf("") }
    var showSelectionDialog by remember { mutableStateOf(true) }

    // Generar un ID aleatorio al entrar
    val randomID = remember { generateRandomID() }

    // Declarar correctamente el focusManager y el keyboardController
    val focusManager = LocalFocusManager.current
    val keyboardController = LocalSoftwareKeyboardController.current

    Box(modifier = Modifier.fillMaxSize()) {
        // Mostrar diálogo de selección de objetos
        if (showSelectionDialog) {
            ObjectSelectionScreen { obj ->
                selectedObject = obj
                showSelectionDialog = false
            }
        }

        // Mostrar DrawingView solo si es pintor
        if (isPainter) {
            // Dibujo
            AndroidView(
                factory = { drawingView },
                modifier = Modifier.fillMaxSize()
            )

            // Texto que muestra el objeto seleccionado
            Text(
                text = "Objeto: $selectedObject",
                modifier = Modifier
                    .align(Alignment.BottomEnd) // Cambiado a BottomEnd para alineación a la derecha
                    .padding(16.dp),
                style = MaterialTheme.typography.titleLarge.copy(color = Color.Black)
            )

            // Paleta de colores y botón de borrador
            Row(
                modifier = Modifier
                    .align(Alignment.TopCenter)
                    .padding(16.dp)
            ) {

                Box(
                    modifier = Modifier
                        .size(50.dp)
                        .clip(CircleShape)
                        .background(Color(0xFFD3D3D3)) // Color gris claro
                        .border(2.dp, Color.Black, CircleShape)
                        .clickable {
                            drawingView.setColor(android.graphics.Color.LTGRAY)  // Establecer el color como gris claro para "borrar"
                        },
                    contentAlignment = Alignment.Center // Centra el contenido dentro del botón
                ) {
                    Text(text = "B", color = Color.Black) // Texto dentro del botón
                }
                Spacer(modifier = Modifier.width(8.dp))
                // Paleta de colores
                ColorButton(Color.Yellow, android.graphics.Color.YELLOW) { drawingView.setColor(it) }
                Spacer(modifier = Modifier.width(8.dp))
                ColorButton(Color.Blue, android.graphics.Color.BLUE) { drawingView.setColor(it) }
                Spacer(modifier = Modifier.width(8.dp))
                ColorButton(Color.Red, android.graphics.Color.RED) { drawingView.setColor(it) }
                Spacer(modifier = Modifier.width(8.dp))
                ColorButton(Color(0xFF8033FF), android.graphics.Color.rgb(128, 51, 255)) { drawingView.setColor(it) }
                Spacer(modifier = Modifier.width(8.dp))
                ColorButton(Color.Green, android.graphics.Color.GREEN) { drawingView.setColor(it) }
                Spacer(modifier = Modifier.width(8.dp))
                ColorButton(Color(0xFFFFA500), android.graphics.Color.rgb(255, 165, 0)) { drawingView.setColor(it) }
                Spacer(modifier = Modifier.width(8.dp))
                ColorButton(Color.Black, android.graphics.Color.BLACK) { drawingView.setColor(it) }
            }
        } else {
            // Mostrar chat si no es pintor
            Column(
                modifier = Modifier
                    .align(Alignment.BottomCenter)
                    .padding(16.dp)
                    .fillMaxWidth()
            ) {
                // ChatBox modificado
                BasicTextField(
                    value = textState,
                    onValueChange = { newText -> textState = newText },
                    modifier = Modifier
                        .fillMaxWidth()
                        .background(Color.White)
                        .padding(8.dp),
                    keyboardOptions = KeyboardOptions.Default.copy(imeAction = ImeAction.Done),
                    keyboardActions = KeyboardActions(
                        onDone = {
                            focusManager.clearFocus()  // Correcto uso del focusManager
                            keyboardController?.hide()  // Correcto uso del keyboardController
                        }
                    )
                )

                Spacer(modifier = Modifier.height(8.dp))

                Button(
                    onClick = {
                        if (textState.isNotEmpty()) {
                            println("Texto enviado: $textState")
                            textState = ""  // Limpia el campo de texto después de enviar

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
        }

        // Mostrar ID aleatorio en lugar del nombre del jugador
        Text(
            text = "ID: $randomID",
            modifier = Modifier
                .align(Alignment.BottomStart)
                .padding(16.dp),
            style = MaterialTheme.typography.titleLarge.copy(color = Color.Black)
        )
    }
}

// Función para generar un ID aleatorio
fun generateRandomID(): String {
    val idLength = 8
    val allowedChars = "ABCDEFGHIJKLMNOPQRSTUVWXYZ0123456789"
    return (1..idLength)
        .map { allowedChars.random() }
        .joinToString("")
}

@Composable
fun ColorButton(color: Color, paintColor: Int, onColorSelected: (Int) -> Unit) {
    Box(
        modifier = Modifier
            .size(50.dp)
            .clip(CircleShape)  // Círculo en lugar de cuadrado
            .background(color)  // Color de fondo
            .border(2.dp, Color.Black, CircleShape)  // Borde alrededor del círculo
            .clickable { onColorSelected(paintColor) }  // Acción al hacer clic
    )
}

// Clase DrawingView para manejar la lógica del lienzo
class DrawingView(context: Context, attrs: AttributeSet?) : View(context, attrs) {
    private var path = Path()
    private var paint = Paint().apply {
        color = android.graphics.Color.BLACK
        style = Paint.Style.STROKE
        strokeWidth = 10f
        isAntiAlias = true
    }

    private var drawCanvas: Canvas? = null
    private var canvasBitmap: Bitmap? = null

    override fun onSizeChanged(w: Int, h: Int, oldw: Int, oldh: Int) {
        super.onSizeChanged(w, h, oldw, oldh)
        canvasBitmap = Bitmap.createBitmap(w, h, Bitmap.Config.ARGB_8888)
        drawCanvas = Canvas(canvasBitmap!!)
    }

    override fun onDraw(canvas: Canvas) {
        super.onDraw(canvas)

        // Establece el fondo gris
        canvas.drawColor(android.graphics.Color.LTGRAY)

        // Dibuja la imagen anterior si existe
        canvasBitmap?.let {
            canvas.drawBitmap(it, 0f, 0f, null)
        }
        canvas.drawPath(path, paint)
    }

    override fun onTouchEvent(event: MotionEvent): Boolean {
        val touchX = event.x
        val touchY = event.y

        when (event.action) {
            MotionEvent.ACTION_DOWN -> {
                path.moveTo(touchX, touchY)
            }
            MotionEvent.ACTION_MOVE -> {
                path.lineTo(touchX, touchY)
            }
            MotionEvent.ACTION_UP -> {
                drawCanvas?.drawPath(path, paint)
                path.reset()
            }
            else -> return false
        }
        invalidate()
        return true
    }

    // Función para cambiar el color del pincel
    fun setColor(newColor: Int) {
        paint.color = newColor
    }

    fun clearCanvas() {
        // Borra el lienzo
        path.reset()
        invalidate() // Redibuja la vista
    }
}
