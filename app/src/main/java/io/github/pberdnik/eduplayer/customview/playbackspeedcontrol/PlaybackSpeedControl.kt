package io.github.pberdnik.eduplayer.customview.playbackspeedcontrol

import android.annotation.SuppressLint
import android.content.Context
import android.graphics.Canvas
import android.util.AttributeSet
import android.view.MotionEvent
import android.view.View
import androidx.core.content.ContextCompat
import androidx.core.content.res.ResourcesCompat
import androidx.core.graphics.withTranslation
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

    private var currentX = 0f

    private fun initView(attrs: AttributeSet?, defStyle: Int) {
        val a = context.obtainStyledAttributes(attrs, R.styleable.PlaybackSpeedControl, defStyle, 0)
        a.recycle()
        invalidateTextPaintAndMeasurements()
    }

    private fun invalidateTextPaintAndMeasurements() {}

    @SuppressLint("ClickableViewAccessibility")
    override fun onTouchEvent(event: MotionEvent?): Boolean {
        if (event == null) {
            return false
        }
        val x = event.x
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

    private val bar = Bar().apply {
        mainColor = ContextCompat.getColor(context, R.color.colorPrimary)
        lineStrokeWidth = resources.displayMetrics.density * 2f
        digitTypeface = ResourcesCompat.getFont(context, R.font.iceland)!!
    }

    override fun onSizeChanged(w: Int, h: Int, oldw: Int, oldh: Int) {
        super.onSizeChanged(w, h, oldw, oldh)
        val contentWidth = (width - paddingLeft - paddingRight).toFloat()
        val contentHeight = (height - paddingTop  - paddingBottom).toFloat()
        bar.setParams(w = contentWidth, h = contentHeight)
    }

    override fun onDraw(canvas: Canvas) {
        super.onDraw(canvas)
        bar.activeX = currentX - paddingStart
        canvas.withTranslation(paddingLeft.toFloat(), paddingTop.toFloat()) {
            bar.draw(canvas)
        }
    }
}
