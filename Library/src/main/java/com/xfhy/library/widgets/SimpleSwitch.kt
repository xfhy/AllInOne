package com.xfhy.library.widgets

import android.content.Context
import android.graphics.Canvas
import android.graphics.Paint
import android.util.AttributeSet
import android.view.View
import com.blankj.utilcode.util.ConvertUtils.dp2px
import com.xfhy.library.R

/**
 * @author : xfhy
 * Create time : 2022/4/26 上午10:21
 * Description : 简单的开关
 */
class SimpleSwitch @JvmOverloads constructor(context: Context, attrs: AttributeSet? = null, defStyleAttr: Int = 0) :
    View(context, attrs, defStyleAttr) {

    companion object {
        private const val DEFAULT_SWITCH_OFF_COLOR = 0xffd8d8d8.toInt()
        private const val DEFAULT_SWITCH_ON_COLOR = 0xffffac26.toInt()
        private const val DEFAULT_SWITCH_CIRCLE_COLOR = 0xffffffff.toInt()
        private val space = dp2px(4f)
    }

    //下面一根线，上面一个圆
    //2个画笔，
    //3个颜色：底层选中颜色，底层未选中颜色，上层选中颜色

    private val mLinePaint = Paint(Paint.ANTI_ALIAS_FLAG)
    private val mCirclePaint = Paint(Paint.ANTI_ALIAS_FLAG)
    private var mLineOffColor = DEFAULT_SWITCH_OFF_COLOR
    private var mLineOnColor = DEFAULT_SWITCH_ON_COLOR
    private var mCircleColor = DEFAULT_SWITCH_CIRCLE_COLOR

    private var mWidth = 0
    private var mHeight = 0
    private var mRadius = 0f

    var isOn = false
        set(value) {
            field = value
            changeListener?.invoke(value)
            invalidate()
        }
    var changeListener: ((isOn: Boolean) -> Unit)? = null

    init {
        val array = context.theme.obtainStyledAttributes(attrs, R.styleable.SimpleSwitch, 0, 0)
        mLineOffColor = array.getColor(R.styleable.SimpleSwitch_switch_bottom_layer_off_color, DEFAULT_SWITCH_OFF_COLOR)
        mLineOnColor = array.getColor(R.styleable.SimpleSwitch_switch_bottom_layer_on_color, DEFAULT_SWITCH_ON_COLOR)
        mCircleColor = array.getColor(R.styleable.SimpleSwitch_switch_circle_color, DEFAULT_SWITCH_CIRCLE_COLOR)
        array.recycle()

        mLinePaint.color = mLineOffColor
        mCirclePaint.color = mCircleColor
        mCirclePaint.style = Paint.Style.FILL
        mLinePaint.style = Paint.Style.FILL
        mLinePaint.strokeCap = Paint.Cap.ROUND

        setOnClickListener {
            isOn = !isOn
            invalidate()
        }
    }

    override fun onSizeChanged(w: Int, h: Int, oldw: Int, oldh: Int) {
        super.onSizeChanged(w, h, oldw, oldh)
        mWidth = w
        mHeight = h
        mLinePaint.strokeWidth = mHeight.toFloat()
        mRadius = (mHeight.toFloat() - space) / 2f
    }

    override fun onDraw(canvas: Canvas?) {
        super.onDraw(canvas)
        drawLine(canvas)
        drawCircle(canvas)
    }

    private fun drawCircle(canvas: Canvas?) {
        if (isOn) {
            canvas?.drawCircle(space / 2f + mRadius, mHeight / 2f, mRadius, mCirclePaint)
        } else {
            canvas?.drawCircle(mWidth - mRadius - space / 2f, mHeight / 2f, mRadius, mCirclePaint)
        }
    }

    private fun drawLine(canvas: Canvas?) {
        if (isOn) {
            mLinePaint.color = mLineOnColor
        } else {
            mLinePaint.color = mLineOffColor
        }
        canvas?.drawLine(mHeight / 2f, mHeight / 2f, mWidth.toFloat() - mHeight / 2f, mHeight / 2f, mLinePaint)
    }

}