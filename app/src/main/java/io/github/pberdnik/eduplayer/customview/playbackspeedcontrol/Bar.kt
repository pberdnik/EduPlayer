package io.github.pberdnik.eduplayer.customview.playbackspeedcontrol

import android.graphics.*
import androidx.core.graphics.ColorUtils

data class Line(val x: Float, val height: Float, val strokeWidth: Float)

class Bar {
    var w = 1000f
    var h = 100f
    var mainColor = Color.BLUE
    var activeX = 500f
    var lineStrokeWidth = 3f
    lateinit var digitTypeface: Typeface

    private val textBounds = Rect()

    fun setParams(w: Float, h: Float) {
        this.w = w
        this.h = h
        invalidate()
    }

    private val activePaint by lazy {
        Paint().apply {
            isAntiAlias = true
            color = mainColor
            strokeWidth = lineStrokeWidth
            strokeCap = Paint.Cap.ROUND
            style = Paint.Style.STROKE
            setShadowLayer(5f, 0f, 0f, mainColor)
        }
    }

    private val inactivePaint by lazy {
        Paint().apply {
            isAntiAlias = true
            color = ColorUtils.blendARGB(mainColor, Color.WHITE, 0.8f)
            strokeWidth = lineStrokeWidth
            strokeCap = Paint.Cap.ROUND
            style = Paint.Style.STROKE
        }
    }

    private val digitPaint by lazy {
        Paint().apply {
            color = mainColor
            isAntiAlias = true
            typeface = digitTypeface
            setShadowLayer(lineStrokeWidth, 0f, 0f, mainColor)
        }
    }

    private val lines = mutableListOf<Line>()

    fun invalidate() {
        lines.clear()
        // lines form a logarithmic scale from 0.25 to 4
        // which transforms to linear log2(0.25) to log2(4), i.e. -2..2
        // hence 2-(-2)=4 for scale=w/4 and the following calculations
        val scale = w / 4
        for (i in 4..64) {
            if (i == 16) continue // skip 1x
            lines.add(
                Line(
                    x = ((Math.log(i / 16.toDouble()) / Math.log(2.toDouble()) + 2) * scale).toFloat(),
                    height = if (i % 16 == 0) h else if (i % 8 == 0) h * 0.66f else h * 0.33f,
                    strokeWidth = if (i % 16 == 0) lineStrokeWidth else lineStrokeWidth / 2
                )
            )
        }
    }

    fun draw(canvas: Canvas) {
        drawLines(canvas)
        drawDigitsOnMainLines(canvas)
    }

    private fun drawLines(canvas: Canvas) {
        for (line in lines) {
            val paint = if (line.x < activeX) activePaint else inactivePaint
            paint.strokeWidth = line.strokeWidth
            canvas.drawLine(line.x, 0f, line.x, line.height, paint)
        }
    }

    private fun drawDigitsOnMainLines(canvas: Canvas) {
        digitPaint.run {
            textSize = h / 2
            textAlign = Paint.Align.CENTER
            setShadowLayer(lineStrokeWidth, 0f, 0f, mainColor)
        }
        var digits = "0.25x"
        digitPaint.getTextBounds(digits, 0, digits.length, textBounds)
        var textX = lines[0].x
        var textY = h - textBounds.bottom
        canvas.drawText(digits, textX, textY, digitPaint)

        digitPaint.run {
            textSize = h * 1.5f
            textAlign = Paint.Align.CENTER
            if (activeX < w * 0.49) clearShadowLayer()
        }
        digits = "1x"
        textX = w / 2
        textY = h
        canvas.drawText(digits, textX, textY, digitPaint)

        digitPaint.run {
            textSize = h
            textAlign = Paint.Align.LEFT
            if (activeX < w) clearShadowLayer()
        }
        digits = "4x"
        digitPaint.getTextBounds(digits, 0, digits.length, textBounds)
        textX = lines[lines.size - 1].x + textBounds.exactCenterX() / 2
        textY = h * 0.8f + textBounds.bottom
        canvas.drawText(digits, textX, textY, digitPaint)
    }
}