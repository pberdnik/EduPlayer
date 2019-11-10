package io.github.pberdnik.eduplayer.customview

import android.content.Context
import android.util.AttributeSet
import android.view.GestureDetector
import android.view.MotionEvent
import android.view.ScaleGestureDetector
import android.widget.ImageView
import kotlin.math.abs


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


    var onChangeListener: OnChangeListener? = null

    private var scaleFactor = 1f
        set(value) {
            field = value.coerceIn(0.1f, 5f)
        }
    private var translateX = 0f
        set(value) {
            val max = width / 2f
            field = value.coerceIn(-max, max)
        }
    private var translateY = 0f
        set(value) {
            val max = height / 2f
            field = value.coerceIn(-max, max)
        }
    private var rewindFactor = 0f

    private val scaleListener = object : ScaleGestureDetector.SimpleOnScaleGestureListener() {
        private var lastFocusX = 0f
        private var lastFocusY = 0f
        private var lastScaleFactor = 1f

        override fun onScaleBegin(detector: ScaleGestureDetector): Boolean {
            lastFocusX = detector.focusX
            lastFocusY = detector.focusY
            lastScaleFactor = scaleFactor
            return true
        }

        override fun onScale(detector: ScaleGestureDetector): Boolean {
            scaleFactor *= detector.scaleFactor
            // Don't let the object get too small or too large.
            when {
                lastScaleFactor > 1f -> scaleFactor = scaleFactor.coerceIn(1f, 5f)
                lastScaleFactor < 1f -> scaleFactor = scaleFactor.coerceIn(0.1f, 1f)
            }

            translateX += detector.focusX - lastFocusX
            translateY += detector.focusY - lastFocusY
            lastFocusX = detector.focusX
            lastFocusY = detector.focusY
            onChangeListener?.onLocationChanged(
                this@GesturePlayerControlView,
                scaleFactor,
                applyMagnetEffect(translateX),
                applyMagnetEffect(translateY)
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
                        applyMagnetEffect(translateX),
                        applyMagnetEffect(translateY)
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

    private val tooSmallTranslation = resources.displayMetrics.density * 16

    private fun applyMagnetEffect(translateCoord: Float)=
        if (scaleFactor == 1f && abs(translateCoord) < tooSmallTranslation) 0f else translateCoord


    private val scaleDetector = ScaleGestureDetector(context, scaleListener)
    private val gestureDetector = GestureDetector(context, gestureListener)

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