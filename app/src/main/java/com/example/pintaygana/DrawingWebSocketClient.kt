package com.example.pintaygana

import android.util.Log
import org.java_websocket.client.WebSocketClient
import org.java_websocket.handshake.ServerHandshake
import org.json.JSONObject
import java.net.URI
import java.util.UUID

class DrawingWebSocketClient(serverUri: URI) : WebSocketClient(serverUri) {
    // Genera un ID único para el usuario
    private val userId: String = UUID.randomUUID().toString()

    override fun onOpen(handshakedata: ServerHandshake?) {
        Log.d("WebSocket", "Conexión abierta")

        // Envía el ID del usuario al servidor cuando se abre la conexión
        val json = JSONObject().apply {
            put("type", "user_connected")
            put("userId", userId)  // Enviar el ID único
        }
        send(json.toString())  // Envía el JSON al servidor
    }

    override fun onMessage(message: String?) {
        // Manejar los mensajes recibidos (actualizar el dibujo)
        println("Mensaje recibido: $message")
    }

    override fun onClose(code: Int, reason: String?, remote: Boolean) {
        println("Conexión cerrada con el código $code por la razón: $reason")
    }

    override fun onError(ex: Exception?) {
        ex?.printStackTrace()
    }
}
