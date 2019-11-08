package io.github.pberdnik.eduplayer.customview

import android.content.Context
import android.graphics.Color
import android.graphics.Paint
import android.util.AttributeSet
import android.view.GestureDetector
import android.view.MotionEvent
import android.view.ScaleGestureDetector
import android.widget.ImageView
import java.lang.Math.abs


class GesturePlayerControlView : ImageView {
    interface OnChangeListener {
        fun onLocationChanged(
            playerControlsView: GesturePlayerControlView,
            scaleFactor: Float,
            translateX: Float,
            translateY: Float
        ) {
        }

        fun onRewindFactorChanged(
            playerControlsView: GesturePlayerControlView,
            rewindFactor: Float,
            isLastChange: Boolean
        ) {
        }

        fun onSingleTap(playerControlsView: GesturePlayerControlView) {}
    }

    constructor(context: Context) : super(context)

    constructor(context: Context, attrs: AttributeSet) : super(context, attrs)

    constructor(context: Context, attrs: AttributeSet, defStyle: Int) :
            super(context, attrs, defStyle)


    var onChangeListener: GesturePlayerControlView.OnChangeListener? = null

    private var scaleFactor = 1f
    private var translateX = 0f
    private var translateY = 0f
    private var rewindFactor = 0f

    private val scaleListener = object : ScaleGestureDetector.SimpleOnScaleGestureListener() {
        private var lastFocusX = 0f
        private var lastFocusY = 0f

        override fun onScaleBegin(detector: ScaleGestureDetector): Boolean {
            lastFocusX = detector.focusX
            lastFocusY = detector.focusY
            return true
        }

        override fun onScale(detector: ScaleGestureDetector): Boolean {
            scaleFactor *= detector.scaleFactor
            // Don't let the object get too small or too large.
            scaleFactor = Math.max(0.1f, Math.min(scaleFactor, 5.0f))

            translateX += detector.focusX - lastFocusX
            translateY += detector.focusY - lastFocusY
            lastFocusX = detector.focusX
            lastFocusY = detector.focusY
            onChangeListener?.onLocationChanged(
                this@GesturePlayerControlView,
                scaleFactor,
                translateX,
                translateY
            )

            invalidate()
            return true
        }

    }

    private val gestureListener = object : GestureDetector.SimpleOnGestureListener() {
        override fun onScroll(
            e1: MotionEvent,
            e2: MotionEvent,
            distanceX: Float,
            distanceY: Float
        ): Boolean {
            if (scaleDetector.isInProgress) {
                // don't allow scrolling while scaling
                return false
            }
            when (e2.pointerCount) {
                1 -> if (canDetectOneFingerScroll) {
                    rewindFactor = (e2.x - e1.x) / width
                    if (abs(rewindFactor) >= 0.1f) {
                        isScrolling = true
                        onChangeListener?.onRewindFactorChanged(
                            this@GesturePlayerControlView,
                            rewindFactor,
                            false
                        )
                    }
                }
                2 -> {
                    translateX -= distanceX
                    translateY -= distanceY
                    onChangeListener?.onLocationChanged(
                        this@GesturePlayerControlView,
                        scaleFactor,
                        translateX,
                        translateY
                    )
                }
            }

            invalidate()
            return true
        }

        override fun onSingleTapUp(e: MotionEvent?): Boolean {
            onChangeListener?.onSingleTap(this@GesturePlayerControlView)
            return true
        }
    }

    private val scaleDetector = ScaleGestureDetector(context, scaleListener)
    private val gestureDetector = GestureDetector(context, gestureListener)

    private val mainPaint by lazy {
        Paint(Paint.ANTI_ALIAS_FLAG).apply {
            color = Color.BLUE
            strokeWidth = 2f
            strokeCap = Paint.Cap.ROUND
            style = Paint.Style.STROKE
            setShadowLayer(5f, 0f, 0f, Color.BLUE)
        }
    }

    private var lastPointerCount = 0
    private var canDetectOneFingerScroll = false
    private var isScrolling = false

    override fun onTouchEvent(event: MotionEvent): Boolean {
        updatePointersInfo(event)
        detectScrollEnd(event)
        var retVal = scaleDetector.onTouchEvent(event)
        retVal = gestureDetector.onTouchEvent(event) || retVal
        return retVal || super.onTouchEvent(event)
    }

    private fun updatePointersInfo(event: MotionEvent) {
        var currentPointerCount = event.pointerCount
        if (event.actionMasked == MotionEvent.ACTION_UP) {
            currentPointerCount = 0
        }
        canDetectOneFingerScroll =
            currentPointerCount == 1 && lastPointerCount == 0
                    || canDetectOneFingerScroll && currentPointerCount == 1 && lastPointerCount == 1
        lastPointerCount = currentPointerCount
    }

    private fun detectScrollEnd(event: MotionEvent) {
        if (isScrolling && event.actionMasked == MotionEvent.ACTION_UP) {
            isScrolling = false
            onChangeListener?.onRewindFactorChanged(
                this,
                rewindFactor = 0f,
                isLastChange = true
            )
        }
    }
}