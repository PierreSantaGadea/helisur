package com.helisur.helisurapp.ui.mantenimiento.inspeccion100horas

import android.content.Context
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.util.AttributeSet
import android.view.View
import com.helisur.helisurapp.R

class DiagonalLineView @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = 0
) : View(context, attrs, defStyleAttr) {

    private val paint = Paint(Paint.ANTI_ALIAS_FLAG).apply {
        style = Paint.Style.STROKE
        color = 0xFF000000.toInt()
        strokeWidth = dpToPx(2f)
    }

    init {
        // Aquí evitamos usar `return` — en su lugar usamos `let` para ejecutar solo si attrs != null
        attrs?.let { aset ->
            val a = context.obtainStyledAttributes(aset, R.styleable.DiagonalLineView, defStyleAttr, 0)
            try {
                paint.color = a.getColor(R.styleable.DiagonalLineView_lineColor, paint.color)
                paint.strokeWidth = a.getDimension(R.styleable.DiagonalLineView_lineWidth, paint.strokeWidth)
            } finally {
                a.recycle()
            }
        }
    }

    private fun dpToPx(dp: Float): Float =
        dp * resources.displayMetrics.density

    override fun onDraw(canvas: Canvas) {
        super.onDraw(canvas)
        val startX = paddingLeft.toFloat()
        val startY = paddingTop.toFloat()
        val endX = (width - paddingRight).toFloat()
        val endY = (height - paddingBottom).toFloat()
        canvas.drawLine(startX, startY, endX, endY, paint)
    }
}