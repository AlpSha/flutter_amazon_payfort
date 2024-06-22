package com.vvvirani.amazon_payfort

import android.animation.ValueAnimator
import android.content.Context
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.graphics.RectF
import android.util.AttributeSet
import android.view.MotionEvent
import android.view.View
import android.view.animation.AccelerateDecelerateInterpolator

class CupertinoSwitch @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = 0
) : View(context, attrs, defStyleAttr) {

    private var switchPaint: Paint = Paint(Paint.ANTI_ALIAS_FLAG)
    private var switchRect: RectF = RectF()
    private var thumbPaint: Paint = Paint(Paint.ANTI_ALIAS_FLAG)
    private var thumbRect: RectF = RectF()

    private var switchOn = false
    private var thumbRadius = 0f
    private var thumbPosition = 0f

    private var activeTrackColor: Int = Color.GREEN
    private var inactiveTrackColor: Int = Color.LTGRAY
    private var thumbColor: Int = Color.WHITE
    private var thumbPadding = 6f // Adjust this value for padding

    private var animator: ValueAnimator? = null

    init {
        switchPaint.color = inactiveTrackColor
        thumbPaint.color = thumbColor
    }

    override fun onMeasure(widthMeasureSpec: Int, heightMeasureSpec: Int) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec)
        val width = MeasureSpec.getSize(widthMeasureSpec)
        val height = MeasureSpec.getSize(heightMeasureSpec)
        setMeasuredDimension(width, height)

        thumbRadius = (height / 2f) - thumbPadding
        thumbPosition = if (switchOn) width.toFloat() - (thumbRadius + thumbPadding) else thumbRadius + thumbPadding
        switchPaint.color = if (switchOn) activeTrackColor else inactiveTrackColor
        switchRect.set(0f, 0f, width.toFloat(), height.toFloat())
        updateThumbRect()
    }

    private fun updateThumbRect() {
        val thumbX = switchRect.left + thumbPosition
        thumbRect.set(
            thumbX - thumbRadius,
            switchRect.top + thumbPadding,
            thumbX + thumbRadius,
            switchRect.bottom - thumbPadding
        )
    }

    override fun onDraw(canvas: Canvas) {
        super.onDraw(canvas)
        canvas.drawRoundRect(switchRect, thumbRadius + thumbPadding, thumbRadius + thumbPadding, switchPaint)
        canvas.drawRoundRect(thumbRect, thumbRadius, thumbRadius, thumbPaint)
    }

    override fun onTouchEvent(event: MotionEvent): Boolean {
        when (event.action) {
            MotionEvent.ACTION_DOWN -> return true
            MotionEvent.ACTION_UP -> {
                switchOn = !switchOn
                animateThumb()
                return true
            }
        }
        return super.onTouchEvent(event)
    }

    private fun animateThumb() {
        val start = if (switchOn) thumbRadius + thumbPadding else switchRect.width() - (thumbRadius + thumbPadding)
        val end = if (switchOn) switchRect.width() - (thumbRadius + thumbPadding) else thumbRadius + thumbPadding

        animator?.cancel()
        animator = ValueAnimator.ofFloat(start, end).apply {
            duration = 300
            interpolator = AccelerateDecelerateInterpolator()
            addUpdateListener { animation ->
                thumbPosition = animation.animatedValue as Float
                updateThumbRect()
                invalidate()
            }
            start()
        }
        switchPaint.color = if (switchOn) activeTrackColor else inactiveTrackColor
    }

    fun setActiveTrackColor(color: Int) {
        activeTrackColor = color
        if (switchOn) {
            switchPaint.color = activeTrackColor
            invalidate()
        }
    }

    fun setInactiveTrackColor(color: Int) {
        inactiveTrackColor = color
        if (!switchOn) {
            switchPaint.color = inactiveTrackColor
            invalidate()
        }
    }

    fun setSwitchOn(isOn: Boolean) {
        switchOn = isOn
        // Update thumb position without animation
        thumbPosition = if (switchOn) switchRect.width() - (thumbRadius + thumbPadding) else thumbRadius + thumbPadding
    }

    fun setThumbColor(color: Int) {
        thumbColor = color
        thumbPaint.color = thumbColor
        invalidate()
    }
}
