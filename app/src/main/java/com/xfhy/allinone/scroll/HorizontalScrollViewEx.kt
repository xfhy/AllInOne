package com.xfhy.allinone.scroll

import android.content.Context
import android.util.AttributeSet
import android.view.MotionEvent
import android.widget.LinearLayout
import com.xfhy.library.ext.log
import kotlin.math.abs

/**
 * @author : xfhy
 * Create time : 2021/2/4 15:43
 * Description : 可简单横向滑动的LinearLayout,已解决滑动冲突
 */

private const val TAG = "xfhy_scroll"

class HorizontalScrollViewEx @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = 0
) : LinearLayout(context, attrs, defStyleAttr) {

    //上次滑动的坐标
    private var mLastX = 0
    private var mLastY = 0

    //上次滑动的坐标    nInterceptTouchEvent
    private var mLastXIntercept = 0
    private var mLastYIntercept = 0

    init {
        orientation = HORIZONTAL
    }

    //1. 水平滑动距离>垂直滑动  则父类消费该事件（拦截下来），否则交给子类消费
    //2. 让LinearLayout可滑动
    override fun onInterceptTouchEvent(ev: MotionEvent): Boolean {
        var intercepted = false
        log(TAG, "onInterceptTouchEvent ${ev.action}")
        val x = ev.x.toInt()
        val y = ev.y.toInt()
        when (ev.action) {
            MotionEvent.ACTION_DOWN -> {  //0
                //down时不拦截,这里拦截了的话,后面的move和up事件子类都收不到了
                intercepted = false
            }
            MotionEvent.ACTION_MOVE -> {  //2   子类有消耗事件的时候才走这里
                val delayX = x - mLastXIntercept
                val delayY = y - mLastYIntercept
                intercepted = abs(delayX) > abs(delayY)
                log(TAG, "mIntercepted = $intercepted  delayX = $delayX   delayY = $delayY")
            }
            MotionEvent.ACTION_UP -> {  //1
                //up时不拦截,这里拦截的话,子类就收不到onClick事件了
                intercepted = false
            }
            else -> {
            }
        }
        mLastX = x
        mLastY = y
        mLastXIntercept = x
        mLastYIntercept = y
        log(TAG, "return mIntercepted = $intercepted")
        return intercepted
    }

    override fun onTouchEvent(event: MotionEvent): Boolean {
        log(TAG, "return onTouchEvent = ${event.action}")

        val x = event.x.toInt()
        val y = event.y.toInt()

        when (event.action) {
            MotionEvent.ACTION_DOWN -> {
            }
            MotionEvent.ACTION_MOVE -> {
                val deltaX = x - mLastX
                val deltaY = y - mLastY

                scrollBy(-deltaX, 0)
            }
            MotionEvent.ACTION_UP -> {
            }
            else -> {
            }
        }

        mLastX = x
        mLastY = y
        return true
    }

}