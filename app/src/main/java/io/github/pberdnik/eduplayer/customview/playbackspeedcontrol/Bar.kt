package io.github.pberdnik.eduplayer.customview.playbackspeedcontrol

import android.graphics.*
import androidx.core.graphics.ColorUtils
import androidx.core.graphics.withTranslation
import androidx.core.math.MathUtils
import kotlin.math.ln
import kotlin.math.max
import kotlin.math.pow

private const val eps = 1f

private data class Line(val x: Float, val height: Float, val strokeWidth: Float)

class Bar {
    var mainColor = Color.BLUE
    var density = 1f
        set(value) {
            field = max(1f, value - fakePaddingLeft)
        }
    var speedValue: Float = 1f

    private val activeX
        get() = (ln(speedValue) / ln(2f) + 2) / 4 * w + eps

    fun speedValueByCurrentX(currentX: Float): Float {
        val percent = MathUtils.clamp(currentX - fakePaddingLeft, 0f, w) / w.toDouble()
        return 2.toDouble().pow(percent * 4 - 2).toFloat()
    }

    private var w = 1000f
    private var h = 100f

    lateinit var digitTypeface: Typeface

    private lateinit var cacheBitmap: Bitmap
    private lateinit var cacheCanvas: Canvas

    private val textBounds = Rect()

    private var lineStrokeWidth = density * w * 0.001f

    // room for digits and shadow
    private var fakePaddingLeft = h
    private var fakePaddingRight = h
    private var fakePaddingBottom = lineStrokeWidth

    private val activePaint by lazy {
        Paint(Paint.ANTI_ALIAS_FLAG).apply {
            color = mainColor
            strokeWidth = lineStrokeWidth
            strokeCap = Paint.Cap.ROUND
            style = Paint.Style.STROKE
            setShadowLayer(5f, 0f, 0f, mainColor)
        }
    }

    private val inactivePaint by lazy {
        Paint(Paint.ANTI_ALIAS_FLAG).apply {
            color = ColorUtils.blendARGB(mainColor, Color.WHITE, 0.8f)
            strokeWidth = lineStrokeWidth
            strokeCap = Paint.Cap.ROUND
            style = Paint.Style.STROKE
        }
    }

    private val digitPaint by lazy {
        Paint(Paint.ANTI_ALIAS_FLAG).apply {
            color = mainColor
            isAntiAlias = true
            typeface = digitTypeface
            setShadowLayer(lineStrokeWidth / 2, 0f, 0f, mainColor)
        }
    }

    private val clearPaint by lazy {
        Paint(Paint.ANTI_ALIAS_FLAG).apply {
            color = Color.TRANSPARENT
            style = Paint.Style.FILL
            xfermode = PorterDuffXfermode(PorterDuff.Mode.CLEAR)
        }
    }

    private val lines = mutableListOf<Line>()

    init {
        setParams(1000f, 100f)
    }

    fun setParams(w: Float, h: Float) {
        invalidatePadding(w, h)
        this.w = w - fakePaddingLeft - fakePaddingRight
        this.h = h - fakePaddingBottom
        invalidateCacheBitmap(w, h)
        invalidateLines()
    }

    private fun invalidatePadding(w: Float, h: Float) {
        lineStrokeWidth = density * w * 0.001f
        fakePaddingLeft = h
        fakePaddingRight = h * 1.5f
        fakePaddingBottom = lineStrokeWidth
    }

    private fun invalidateCacheBitmap(w: Float, h: Float) {
        cacheBitmap = Bitmap.createBitmap(max(1, w.toInt()), max(1, h.toInt()), Bitmap.Config.ARGB_8888)
        cacheCanvas = Canvas(cacheBitmap)
    }

    private fun invalidateLines() {
        lines.clear()
        // lines form a logarithmic scale from 0.25 to 4
        // which transforms to linear log2(0.25) to log2(4), i.e. -2..2
        // hence 2-(-2)=4 for scale=w/4 and the following calculations
        val scale = w / 4
        for (i in 4..64) {
            if (i == 16) continue // skip 1x special case
            lines.add(
                Line(
                    x = (ln(i / 16f) / ln(2f) + 2) * scale,
                    height = if (i % 16 == 0) h else if (i % 8 == 0) h * 0.66f else h * 0.33f,
                    strokeWidth = if (i % 16 == 0) lineStrokeWidth else lineStrokeWidth / 2
                )
            )
        }
    }

    fun draw(canvas: Canvas) {
        cacheBitmap.eraseColor(Color.TRANSPARENT)
        cacheCanvas.withTranslation(fakePaddingLeft, 0f) { drawLines() }
        canvas.drawBitmap(cacheBitmap, 0f, 0f, null)
        canvas.withTranslation(fakePaddingLeft, 0f) {
            drawDigitsOnMainLines()
        }
    }

    private fun Canvas.drawLines() {
        for (line in lines) {
            val paint = if (line.x < activeX) activePaint else inactivePaint
            paint.strokeWidth = line.strokeWidth
            drawLine(line.x, 0f, line.x, line.height, paint)
        }
        drawSpecialLineFor1x()
    }

    private fun Canvas.drawSpecialLineFor1x() {
        val paint = if (w * 0.49 < activeX) activePaint else inactivePaint
        paint.strokeWidth = lineStrokeWidth
        val r = h * 0.36f
        val cx = w / 2
        val cy = h / 2
        drawCircle(cx, cy, r, clearPaint)
        drawLine(cx, 0f, cx, cy - r, paint)
        drawArc(cx - r, cy - r, cx + r, cy + r, -130f, 80f, false, paint)
        drawArc(cx - r, cy - r, cx + r, cy + r, 60f, 60f, false, paint)
        drawLine(cx, cy + r, cx, h, paint)
    }

    private fun Canvas.drawDigitsOnMainLines() {
        draw025x()
        draw1xWithLine()
        draw4x()
    }

    private fun Canvas.draw025x() {
        digitPaint.apply {
            textSize = h / 2
            textAlign = Paint.Align.CENTER
            setShadowLayer(lineStrokeWidth / 2, 0f, 0f, mainColor)
        }
        val digits = "0.25x"
        digitPaint.getTextBounds(digits, 0, digits.length, textBounds)
        val textX = lines[0].x
        val textY = h - textBounds.bottom
        drawText(digits, textX, textY, digitPaint)
    }

    private fun Canvas.draw1xWithLine() {
        digitPaint.apply {
            textSize = h * 0.7f
            textAlign = Paint.Align.CENTER
            if (activeX < w * 0.49) clearShadowLayer()
        }
        val digits = "1x"
        val textX = w / 2
        val textY = h * 0.66f
        drawText(digits, textX, textY, digitPaint)
    }

    private fun Canvas.draw4x() {
        digitPaint.apply {
            textSize = h
            textAlign = Paint.Align.LEFT
            if (activeX < w) clearShadowLayer()
        }
        val digits = "4x"
        digitPaint.getTextBounds(digits, 0, digits.length, textBounds)
        val textX = lines[lines.size - 1].x + textBounds.exactCenterX() / 2
        val textY = h * 0.8f + textBounds.bottom
        drawText(digits, textX, textY, digitPaint)
    }
}