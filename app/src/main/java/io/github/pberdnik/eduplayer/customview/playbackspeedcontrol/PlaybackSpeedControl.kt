package io.github.pberdnik.eduplayer.customview.playbackspeedcontrol

import android.content.Context
import android.graphics.*
import android.util.AttributeSet
import android.view.MotionEvent
import android.view.View
import androidx.core.content.ContextCompat
import androidx.core.content.res.ResourcesCompat
import androidx.core.graphics.ColorUtils
import io.github.pberdnik.eduplayer.R


class PlaybackSpeedControl : View {

    constructor(context: Context) : super(context) {
        initView(null, 0)
    }

    constructor(context: Context, attrs: AttributeSet) : super(context, attrs) {
        initView(attrs, 0)
    }

    constructor(context: Context, attrs: AttributeSet, defStyle: Int) : super(context, attrs, defStyle) {
        initView(attrs, defStyle)
    }

    var currentX = 0f

    val primaryColor = ContextCompat.getColor(context, R.color.colorPrimary)
    val gradientToPrimary: Shader = LinearGradient(
        0f, 0f, 1f, 0f,
        ColorUtils.blendARGB(primaryColor, Color.WHITE, 0.8f),
        primaryColor,
        Shader.TileMode.REPEAT
    )
    val primaryDarkColor = ContextCompat.getColor(context, R.color.colorPrimaryDark)
    val gradientToPrimaryDark: Shader = LinearGradient(
        0f, 0f, 1f, 0f,
        ColorUtils.blendARGB(primaryDarkColor, Color.WHITE, 0.5f),
        primaryDarkColor,
        Shader.TileMode.REPEAT
    )

    val gradientMatrix = Matrix()

    val cornerPathEffect = CornerPathEffect(resources.displayMetrics.density * 4)

    private val barPaint = Paint().apply {
        isAntiAlias = true
        style = Paint.Style.FILL_AND_STROKE
        shader = gradientToPrimary
        strokeWidth = resources.displayMetrics.density
        pathEffect = cornerPathEffect
    }

    private val circlePaint = Paint().apply {
        isAntiAlias = true
        style = Paint.Style.FILL_AND_STROKE
        shader = gradientToPrimaryDark
    }

    val sliderPaint = Paint().apply {
        isAntiAlias = true
        style = Paint.Style.FILL_AND_STROKE
        shader = gradientToPrimaryDark
        pathEffect = cornerPathEffect
    }

    val sliderPath = Path()
    val barLeftFilledPartPath = Path()
    val barRightEmpryPartPath = Path()

    private val digitPaint = Paint().apply {
        color = primaryDarkColor
        isAntiAlias = true
        typeface = ResourcesCompat.getFont(context, R.font.iceland)
    }

    private fun initView(attrs: AttributeSet?, defStyle: Int) {
        val a = context.obtainStyledAttributes(
            attrs, R.styleable.PlaybackSpeedControl, defStyle, 0
        )
        a.recycle()
        invalidateTextPaintAndMeasurements()
    }

    private fun invalidateTextPaintAndMeasurements() {}

    override fun onTouchEvent(event: MotionEvent?): Boolean {
        if (event == null) {
            return false
        }
        val x = event.x
        val y = event.y
        when (event.action) {
            MotionEvent.ACTION_DOWN -> {
                currentX = x
                invalidate()
            }
            MotionEvent.ACTION_MOVE -> {
                currentX = x
                invalidate()
            }

        }
        return true
    }

    override fun onDraw(canvas: Canvas) {
        super.onDraw(canvas)

        val yStart = paddingTop.toFloat()
        val yEnd = height - paddingBottom.toFloat()
        val xStart = paddingLeft.toFloat()
        val xEnd = width - paddingRight.toFloat()

        val areaH = yEnd - yStart
        val areaW = xEnd - xStart

        val sliderMaxH = areaH
        val sliderMaxW = sliderMaxH * 2.5f
        val sliderMinH = sliderMaxH * 0.4f
        val sliderMinW = sliderMaxW * 0.4f
        val sliderDiffH = sliderMaxH - sliderMinH

        val barMaxH = sliderMaxH * 0.9f
        val barMaxW = sliderMaxW * 0.9f
        val barMinH = sliderMinH * 0.9f
        val barMinW = sliderMinW * 0.9f
        val barDiffH = barMaxH - barMinH
        val barDiffW = barMaxW - barMinW

        val yCenter = yStart + areaH / 2

        val barStart = xStart + sliderMinW / 2
        val barEnd = xEnd - sliderMaxW / 2

        val sliderX = Math.max(barStart, Math.min(currentX, barEnd))

        val percent = (sliderX - barStart) / (barEnd - barStart)

        val barPercentH = barMinH + barDiffH * percent
        val percentYTop = yCenter - barPercentH / 2
        val percentYBottom = yCenter + barPercentH / 2

        barLeftFilledPartPath.run {
            reset()
            moveTo(sliderX, percentYTop)
            lineTo(barStart - barMinW / 2, yCenter - barMinH / 2)
            lineTo(barStart - barMinW / 2, yCenter + barMinH / 2)
            lineTo(sliderX, percentYBottom)
            close()
        }

        barRightEmpryPartPath.run {
            reset()
            moveTo(sliderX, percentYTop)
            lineTo(barEnd + barMaxW / 2, yCenter - barMaxH / 2)
            lineTo(barEnd + barMaxW / 2, yCenter + barMaxH / 2)
            lineTo(sliderX, percentYBottom)
        }

        gradientMatrix.run {
            reset()
            postScale(width.toFloat(), height.toFloat())
        }
        barPaint.shader.setLocalMatrix(gradientMatrix)
        circlePaint.shader.setLocalMatrix(gradientMatrix)

        val sliderHalfW = (sliderMinW + (sliderMaxW - sliderMinW) * percent) / 2
        val sliderHalfLeftH = (sliderMinH + sliderDiffH * ((sliderX - barStart - sliderHalfW) / (barEnd - barStart))) / 2
        val sliderHalfRightH = (sliderMinH + sliderDiffH * percent) / 2
        sliderPath.apply {
            reset()
            moveTo(sliderX - sliderHalfW, yCenter - sliderHalfLeftH)
            lineTo(sliderX - sliderHalfW, yCenter + sliderHalfLeftH)
            lineTo(sliderX + sliderHalfW, yCenter + sliderHalfRightH)
            lineTo(sliderX + sliderHalfW, yCenter - sliderHalfRightH)
            close()
        }
        canvas.run {

            barPaint.style = Paint.Style.STROKE
            drawPath(barRightEmpryPartPath, barPaint)

            barPaint.style = Paint.Style.FILL
            drawPath(barLeftFilledPartPath, barPaint)

            digitPaint.color = primaryDarkColor

            var str = "4x"
            digitPaint.textSize = barMaxH
            var xOffset = digitPaint.measureText(str) * 0.5f
            var yOffset = digitPaint.fontMetrics.ascent * 0.3f
            var textX = barEnd - xOffset
            var textY = yCenter - yOffset
            drawText(str, textX, textY, digitPaint)

            str = "1x"
            digitPaint.textSize = barMinH + barDiffH * 0.5f
            xOffset = digitPaint.measureText(str) * 0.5f
            yOffset = digitPaint.fontMetrics.ascent * 0.3f
            textX = barStart + (barEnd - barStart) / 2 - xOffset
            textY = yCenter - yOffset
            drawText(str, textX, textY, digitPaint)

            str = "0.25x"
            digitPaint.textSize = barMinH
            xOffset = digitPaint.measureText(str) * 0.5f
            yOffset = digitPaint.fontMetrics.ascent * 0.3f
            textX = barStart - xOffset
            textY = yCenter - yOffset
            drawText(str, textX, textY, digitPaint)

            drawPath(sliderPath, sliderPaint)

            digitPaint.color = Color.WHITE
            val v = Math.pow(2.toDouble(), percent.toDouble() * 4 - 2)
            str = when(v) {
                in 0.995..1.004 -> "1x"
                in 3.995..4.004 -> "4x"
                else -> String.format("%.2fx", v)
            }
            digitPaint.textSize = sliderMinH + sliderDiffH * percent
            xOffset = digitPaint.measureText(str) * 0.5f
            yOffset = digitPaint.fontMetrics.ascent * 0.3f
            textX = sliderX - xOffset
            textY = yCenter - yOffset
            drawText(str, textX, textY, digitPaint)
        }
    }
}
