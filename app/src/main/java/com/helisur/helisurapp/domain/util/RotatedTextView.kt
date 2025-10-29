package com.helisur.helisurapp.domain.util

import android.content.Context
import android.graphics.Canvas
import android.util.AttributeSet
import androidx.appcompat.widget.AppCompatTextView

class RotatedTextView @JvmOverloads constructor(
    context: Context, attrs: AttributeSet? = null, defStyleAttr: Int = 0
) : AppCompatTextView(context, attrs, defStyleAttr) {

    /** 90 = abajo→arriba (como en tu captura). 270 = arriba→abajo */
    var angle: Int = 90

    override fun onMeasure(widthMeasureSpec: Int, heightMeasureSpec: Int) {
        // Medimos intercambiando specs para que el padre nos dé el alto como "ancho" y viceversa
        super.onMeasure(heightMeasureSpec, widthMeasureSpec)
        // Intercambiamos dimensiones medidas
        setMeasuredDimension(measuredHeight, measuredWidth)
    }

    override fun onDraw(canvas: Canvas) {
        canvas.save()
        when (angle % 360) {
            90 -> {
                // Coloca el origen a la esquina adecuada y rota dentro del bounds
                canvas.translate(width.toFloat(), 0f)
                canvas.rotate(90f)
            }
            270 -> {
                canvas.translate(0f, height.toFloat())
                canvas.rotate(270f)
            }
            else -> { /* otros ángulos si hiciera falta */ }
        }
        super.onDraw(canvas)
        canvas.restore()
    }
}