package io.github.pberdnik.eduplayer.customview.playbackspeedcontrol

import android.annotation.SuppressLint
import android.content.Context
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.PorterDuff
import android.util.AttributeSet
import android.util.Log
import android.view.GestureDetector
import android.view.MotionEvent
import android.view.View
import androidx.core.content.ContextCompat
import androidx.core.content.res.ResourcesCompat
import androidx.core.graphics.withTranslation
import androidx.core.math.MathUtils
import io.github.pberdnik.eduplayer.R
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import kotlin.math.min


private const val HIDE_TIMEOUT = 3_000L

class PlaybackSpeedControl : View {

    interface OnChangeListener {
        fun onSpeedValueChanged(playbackSpeedControl: PlaybackSpeedControl, speedValue: Float) {}
    }

    constructor(context: Context) : super(context) {
        initView(null, 0)
    }

    constructor(context: Context, attrs: AttributeSet) : super(context, attrs) {
        initView(attrs, 0)
    }

    constructor(context: Context, attrs: AttributeSet, defStyle: Int) : super(
        context,
        attrs,
        defStyle
    ) {
        initView(attrs, defStyle)
    }

    private var viewCoroutineScope = CoroutineScope(SupervisorJob() + Dispatchers.Main.immediate)
    private var hidePscJob: Job? = null

    var onChangeListener: OnChangeListener? = null

    var color = ContextCompat.getColor(context, R.color.colorPrimary)
        set(value) {
            field = value
            bar.mainColor = color
            invalidate()
        }

    var digitsColor = ContextCompat.getColor(context, R.color.colorPrimary)
        set(value) {
            field = value
            bar.digitsColor = digitsColor
            invalidate()
        }

    var onColor = ContextCompat.getColor(context, R.color.colorPrimary)
        set(value) {
            field = value
            bar.onColor = onColor
            invalidate()
        }

    private var isTransparent = true
        set(value) {
            field = value
            invalidate()
        }

    // values dependencies: currentX => speedValue => bar.speedValue

    var speedValue = 1f
        set(value) {
            val v = MathUtils.clamp(value, 0.25f, 4f)
            field = v
            bar.speedValue = v
            onChangeListener?.onSpeedValueChanged(this, v)
            invalidate()
        }

    private var currentX = 0f
        set(value) {
            field = value
            speedValue = bar.speedValueByCurrentX(currentX - paddingLeft)
        }

    private val optimalRatio = 1 / 10f
    private val minRatio = 1 / 40f
    private val maxRatio = 1 / 4f
    private val optimalW = (200 * resources.displayMetrics.density).toInt()
    private val optimalH = (optimalW * optimalRatio).toInt()

    private val gestureListener = object : GestureDetector.SimpleOnGestureListener() {
        override fun onScroll(
            e1: MotionEvent,
            e2: MotionEvent,
            distanceX: Float,
            distanceY: Float
        ): Boolean {
            currentX = e2.x
            invalidate()
            return true
        }

        override fun onSingleTapUp(e: MotionEvent): Boolean {
            currentX = e.x
            invalidate()
            return true
        }

        override fun onDown(e: MotionEvent?): Boolean {
            return true
        }
    }
    private val gestureDetector = GestureDetector(context, gestureListener)

    private fun initView(attrs: AttributeSet?, defStyle: Int) {
        val a = context.obtainStyledAttributes(
            attrs,
            R.styleable.PlaybackSpeedControl,
            defStyle,
            0
        )
        try {
            color = a.getColor(R.styleable.PlaybackSpeedControl_color, color)
            digitsColor = a.getColor(R.styleable.PlaybackSpeedControl_digits_color, color)
            onColor = a.getColor(R.styleable.PlaybackSpeedControl_on_color, color)
            speedValue = a.getFloat(R.styleable.PlaybackSpeedControl_value, speedValue)
        } finally {
            a.recycle()
        }
    }

    override fun onMeasure(widthMeasureSpec: Int, heightMeasureSpec: Int) {
        Log.v("onMeasure w", MeasureSpec.toString(widthMeasureSpec))
        Log.v("onMeasure h", MeasureSpec.toString(heightMeasureSpec))

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
                MeasureSpec.UNSPECIFIED -> (min(specW, optimalW)) to (min(
                    specW,
                    optimalW
                ) * optimalRatio).toInt()
                else -> 0 to 0
            }
            MeasureSpec.UNSPECIFIED -> when (specModeH) {
                MeasureSpec.EXACTLY -> (specH / optimalRatio).toInt() to specH
                MeasureSpec.AT_MOST -> (min(specH, optimalH) * optimalRatio).toInt() to (min(
                    specH,
                    optimalH
                ))
                MeasureSpec.UNSPECIFIED ->
                    min(specW, (specH / optimalRatio).toInt()) to min(
                        specH,
                        (specW * optimalRatio).toInt()
                    )
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
        when (event?.action) {
            MotionEvent.ACTION_DOWN -> show()
            MotionEvent.ACTION_UP -> hideOnTimeout()
        }

        return gestureDetector.onTouchEvent(event) || super.onTouchEvent(event)
    }

    fun show() {
        hidePscJob?.cancel()
        isTransparent = false
    }

    fun hide() {
        hideOnTimeout(0L)
    }

    fun hideOnTimeout(timeout: Long = HIDE_TIMEOUT) {
        hidePscJob?.cancel()
        hidePscJob = viewCoroutineScope.launch {
            delay(timeout)
            isTransparent = true
        }
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

        Log.v("onSizeChanged", "$oldw $oldh -> $w $h")

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
        if (isTransparent) {
            canvas.drawColor(Color.TRANSPARENT, PorterDuff.Mode.CLEAR)
            return
        }
        bar.speedValue = speedValue
        canvas.withTranslation(paddingLeft.toFloat(), paddingTop.toFloat()) {
            bar.draw(canvas)
        }
    }
}
