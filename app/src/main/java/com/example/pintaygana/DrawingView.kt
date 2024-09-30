package com.example.pintaygana

import android.content.Context
import android.graphics.*
import android.util.AttributeSet
import android.view.MotionEvent
import android.view.View

class DrawingView(context: Context, attrs: AttributeSet?) : View(context, attrs) {

    private var path = Path()
    private var paint = Paint().apply {
        color = Color.BLACK
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
        canvas.drawColor(Color.LTGRAY)

        // Dibuja la imagen anterior si existe
        canvasBitmap?.let {
            canvas.drawBitmap(it, 0f, 0f, null)
        }

        // Dibuja el camino actual (path)
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

    // Función para borrar el lienzo
    fun clearCanvas() {
        // Mantiene el fondo gris al borrar
        drawCanvas?.drawColor(Color.LTGRAY)
        invalidate()  // Redibuja el lienzo
    }

    // Función para cambiar el color del pincel
    fun setColor(newColor: Int) {
        paint.color = newColor
    }

    class DrawingView(context: Context, attrs: AttributeSet?) : View(context, attrs) {

        private var path = Path()
        private var paint = Paint().apply {
            color = Color.BLACK
            style = Paint.Style.STROKE
            strokeWidth = 10f
            isAntiAlias = true
        }
        private var drawCanvas: Canvas? = null
        private var canvasBitmap: Bitmap? = null

        // Guarda el bitmap actual
        fun getBitmap(): Bitmap? {
            return canvasBitmap
        }

        // Restaura el bitmap guardado
        fun setBitmap(bitmap: Bitmap) {
            canvasBitmap = bitmap
            drawCanvas = Canvas(canvasBitmap!!)
            invalidate()
        }

        override fun onSizeChanged(w: Int, h: Int, oldw: Int, oldh: Int) {
            super.onSizeChanged(w, h, oldw, oldh)
            if (canvasBitmap == null) {
                canvasBitmap = Bitmap.createBitmap(w, h, Bitmap.Config.ARGB_8888)
                drawCanvas = Canvas(canvasBitmap!!)
            }
        }

        override fun onDraw(canvas: Canvas) {
            super.onDraw(canvas)
            canvas.drawColor(Color.LTGRAY)  // Fondo gris
            canvasBitmap?.let {
                canvas.drawBitmap(it, 0f, 0f, null)
            }
            canvas.drawPath(path, paint)
        }

        override fun onTouchEvent(event: MotionEvent): Boolean {
            val touchX = event.x
            val touchY = event.y

            when (event.action) {
                MotionEvent.ACTION_DOWN -> path.moveTo(touchX, touchY)
                MotionEvent.ACTION_MOVE -> path.lineTo(touchX, touchY)
                MotionEvent.ACTION_UP -> {
                    drawCanvas?.drawPath(path, paint)
                    path.reset()
                }
                else -> return false
            }
            invalidate()
            return true
        }

        fun clearCanvas() {
            drawCanvas?.drawColor(Color.LTGRAY)
            invalidate()
        }
    }

}
