package io.github.pberdnik.eduplayer.customview.playbackspeedcontrol

import android.annotation.SuppressLint
import android.content.Context
import android.graphics.Canvas
import android.util.AttributeSet
import android.util.Log
import android.view.MotionEvent
import android.view.View
import androidx.core.content.ContextCompat
import androidx.core.content.res.ResourcesCompat
import androidx.core.graphics.withTranslation
import io.github.pberdnik.eduplayer.R
import kotlin.math.min


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

    var color = ContextCompat.getColor(context, R.color.colorPrimary)
        set(value) {
            field = value
            bar.mainColor = color
            invalidate()
        }

    private var currentX = 0f

    private val optimalRatio = 1 / 10f
    private val minRatio = 1 / 40f
    private val maxRatio = 1 / 4f
    private val optimalW = (200 * resources.displayMetrics.density).toInt()
    private val optimalH = (optimalW * optimalRatio).toInt()

    private fun initView(attrs: AttributeSet?, defStyle: Int) {
        val a = context.obtainStyledAttributes(
            attrs,
            R.styleable.PlaybackSpeedControl,
            defStyle,
            0
        )
        try {
            color = a.getColor(R.styleable.PlaybackSpeedControl_color, color)
        } finally {
            a.recycle()
        }
    }

    override fun onMeasure(widthMeasureSpec: Int, heightMeasureSpec: Int) {
        Log.v("Chart onMeasure w", MeasureSpec.toString(widthMeasureSpec))
        Log.v("Chart onMeasure h", MeasureSpec.toString(heightMeasureSpec))

        val specModeW = MeasureSpec.getMode(widthMeasureSpec)
        val specW = MeasureSpec.getSize(widthMeasureSpec) - paddingLeft - paddingRight
        val specModeH = MeasureSpec.getMode(heightMeasureSpec)
        val specH = MeasureSpec.getSize(heightMeasureSpec) - paddingTop - paddingBottom

        val (desiredW, desiredH) = when (specModeW) {
            MeasureSpec.EXACTLY -> when (specModeH) {
                MeasureSpec.EXACTLY -> specW to specH
                MeasureSpec.AT_MOST -> specW to min(specH, (specW * optimalRatio).toInt())
                MeasureSpec.UNSPECIFIED -> specW to (specW * optimalRatio).toInt()
                else -> 0 to 0
            }
            MeasureSpec.AT_MOST -> when (specModeH) {
                MeasureSpec.EXACTLY -> min(specW, (specH / optimalRatio).toInt()) to specH
                MeasureSpec.AT_MOST -> run {
                    val desW = min(optimalW, specW)
                    val minH = desW * minRatio
                    val desH = desW * optimalRatio
                    when {
                        specH < minH -> (specH / minRatio).toInt() to specH
                        specH < desH -> (specH / optimalRatio).toInt() to specH
                        else -> desW to desH.toInt()
                    }
                }
                MeasureSpec.UNSPECIFIED -> (min(specW, optimalW)) to (min(specW, optimalW) * optimalRatio).toInt()
                else -> 0 to 0
            }
            MeasureSpec.UNSPECIFIED -> when (specModeH) {
                MeasureSpec.EXACTLY -> (specH / optimalRatio).toInt() to specH
                MeasureSpec.AT_MOST -> (min(specH, optimalH) * optimalRatio).toInt() to (min(specH, optimalH))
                MeasureSpec.UNSPECIFIED ->
                    min(specW, (specH / optimalRatio).toInt()) to min(specH, (specW * optimalRatio).toInt())
                else -> 0 to 0
            }
            else -> 0 to 0
        }

        setMeasuredDimension(
            desiredW + paddingLeft + paddingRight,
            desiredH + paddingTop + paddingBottom
        )
    }

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

    private val bar by lazy {
        Bar().apply {
            mainColor = color
            digitTypeface = ResourcesCompat.getFont(context, R.font.iceland)!!
            density = resources.displayMetrics.density
        }
    }

    override fun onSizeChanged(w: Int, h: Int, oldw: Int, oldh: Int) {
        super.onSizeChanged(w, h, oldw, oldh)
        val contentW = (width - paddingLeft - paddingRight).toFloat()
        val contentH = (height - paddingTop - paddingBottom).toFloat()
        val minH = contentW * minRatio
        val maxH = contentW * maxRatio
        val desiredH = min(contentH, maxH)
        if (desiredH < minH) {
            bar.setParams(w = minH / minRatio, h = desiredH)
        } else {
            bar.setParams(w = contentW, h = desiredH)
        }
    }

    override fun onDraw(canvas: Canvas) {
        super.onDraw(canvas)
        bar.activeX = currentX - paddingStart
        canvas.withTranslation(paddingLeft.toFloat(), paddingTop.toFloat()) {
            bar.draw(canvas)
        }
    }
}
