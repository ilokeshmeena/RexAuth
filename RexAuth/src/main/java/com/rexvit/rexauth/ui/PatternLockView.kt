package com.rexvit.rexauth.ui

import android.content.Context
import android.graphics.*
import android.util.AttributeSet
import android.view.MotionEvent
import android.view.View
import androidx.core.content.ContextCompat
import com.example.rexauth.R

class PatternLockView @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = 0
) : View(context, attrs, defStyleAttr) {

    private var dotCount = 3
    private var dotRadius = 20f
    private var pathWidth = 8f
    private var normalColor = Color.GRAY
    private var correctColor = Color.BLUE
    private var wrongColor = Color.RED

    private val dots = mutableListOf<Dot>()
    private val path = Path()
    private val pathPaint = Paint(Paint.ANTI_ALIAS_FLAG)
    private val dotPaint = Paint(Paint.ANTI_ALIAS_FLAG)
    private val selectedDotPaint = Paint(Paint.ANTI_ALIAS_FLAG)

    private var currentPattern = mutableListOf<Int>()
    private var patternListener: OnPatternListener? = null

    init {
        val a = context.obtainStyledAttributes(attrs, R.styleable.PatternLockView)
        dotCount = a.getInt(R.styleable.PatternLockView_plv_dotCount, 3)
        pathWidth = a.getDimension(R.styleable.PatternLockView_plv_pathWidth, 8f)
        normalColor = a.getColor(R.styleable.PatternLockView_plv_colorNormal, Color.GRAY)
        correctColor = a.getColor(R.styleable.PatternLockView_plv_colorCorrect, Color.BLUE)
        wrongColor = a.getColor(R.styleable.PatternLockView_plv_colorWrong, Color.RED)
        a.recycle()

        pathPaint.color = normalColor
        pathPaint.style = Paint.Style.STROKE
        pathPaint.strokeWidth = pathWidth
        pathPaint.strokeJoin = Paint.Join.ROUND
        pathPaint.strokeCap = Paint.Cap.ROUND

        dotPaint.color = normalColor
        selectedDotPaint.color = correctColor
    }

    override fun onMeasure(widthMeasureSpec: Int, heightMeasureSpec: Int) {
        val desiredWidth = suggestedMinimumWidth + paddingLeft + paddingRight
        val desiredHeight = suggestedMinimumHeight + paddingTop + paddingBottom

        val measuredWidth = resolveSize(desiredWidth, widthMeasureSpec)
        val measuredHeight = resolveSize(desiredHeight, heightMeasureSpec)

        setMeasuredDimension(measuredWidth, measuredHeight.coerceAtLeast(measuredWidth))
    }

    override fun onSizeChanged(w: Int, h: Int, oldw: Int, oldh: Int) {
        super.onSizeChanged(w, h, oldw, oldh)
        initDots()
    }

    private fun initDots() {
        dots.clear()
        val width = width.toFloat()
        val height = height.toFloat()
        val spacing = width / (dotCount + 1)

        for (row in 0 until dotCount) {
            for (col in 0 until dotCount) {
                val x = spacing * (col + 1)
                val y = spacing * (row + 1)
                dots.add(Dot(row * dotCount + col, x, y))
            }
        }
    }

    override fun onDraw(canvas: Canvas) {
        super.onDraw(canvas)

        // Draw path
        canvas.drawPath(path, pathPaint)

        // Draw dots
        dots.forEach { dot ->
            val paint = if (currentPattern.contains(dot.id)) selectedDotPaint else dotPaint
            canvas.drawCircle(dot.x, dot.y, dotRadius, paint)
        }
    }

    override fun onTouchEvent(event: MotionEvent): Boolean {
        val x = event.x
        val y = event.y

        when (event.action) {
            MotionEvent.ACTION_DOWN -> {
                resetPattern()
                handleDotTouch(x, y)
                return true
            }
            MotionEvent.ACTION_MOVE -> {
                handleDotTouch(x, y)
                path.lineTo(x, y)
                invalidate()
                return true
            }
            MotionEvent.ACTION_UP -> {
                if (currentPattern.isNotEmpty()) {
                    patternListener?.onPatternComplete(currentPattern)
                }
                return true
            }
        }
        return super.onTouchEvent(event)
    }

    private fun handleDotTouch(x: Float, y: Float) {
        dots.forEach { dot ->
            if (isTouchOnDot(x, y, dot) && !currentPattern.contains(dot.id)) {
                currentPattern.add(dot.id)
                if (currentPattern.size == 1) {
                    path.moveTo(dot.x, dot.y)
                } else {
                    path.lineTo(dot.x, dot.y)
                }
            }
        }
    }

    private fun isTouchOnDot(x: Float, y: Float, dot: Dot): Boolean {
        return Math.sqrt(
            Math.pow((x - dot.x).toDouble(), 2.0) +
                    Math.pow((y - dot.y).toDouble(), 2.0)
        ) <= dotRadius * 2
    }

    fun setPatternListener(listener: OnPatternListener) {
        this.patternListener = listener
    }

    fun setPatternCorrect() {
        pathPaint.color = correctColor
        selectedDotPaint.color = correctColor
        invalidate()
    }

    fun setPatternWrong() {
        pathPaint.color = wrongColor
        selectedDotPaint.color = wrongColor
        invalidate()
        postDelayed({ resetPattern() }, 1000)
    }

    fun resetPattern() {
        path.reset()
        currentPattern.clear()
        pathPaint.color = normalColor
        selectedDotPaint.color = normalColor
        invalidate()
    }

    interface OnPatternListener {
        fun onPatternComplete(pattern: List<Int>)
    }

    private data class Dot(val id: Int, val x: Float, val y: Float)
}