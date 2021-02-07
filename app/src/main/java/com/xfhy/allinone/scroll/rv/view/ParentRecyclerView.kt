package com.xfhy.allinone.scroll.rv.view

import android.content.Context
import android.util.AttributeSet
import android.view.MotionEvent
import androidx.recyclerview.widget.RecyclerView
import com.xfhy.library.ext.log
import kotlin.math.abs

/**
 * @author : xfhy
 * Create time : 2021/2/7 10:50
 * Description :
 */
const val TAG = "xfhy_scroll"
class ParentRecyclerView @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = 0
) : RecyclerView(context, attrs, defStyleAttr) {

    //上次滑动的坐标    nInterceptTouchEvent
    private var mLastXIntercept = 0
    private var mLastYIntercept = 0
    private var mInterceptCallback: (() -> Boolean)? = null

    override fun onInterceptTouchEvent(ev: MotionEvent): Boolean {

        var intercepted = super.onInterceptTouchEvent(ev)

        val x = ev.x.toInt()
        val y = ev.y.toInt()

        when (ev.action) {
            MotionEvent.ACTION_DOWN -> {
                //down时不拦截,这里拦截了的话,后面的move和up事件子类都收不到了
                intercepted = false
            }
            MotionEvent.ACTION_MOVE -> {
                //判断子类是否需要这个事件,如果需要则不拦截
                val delayX = x - mLastXIntercept
                val delayY = y - mLastYIntercept

                //是否为横向滑动  横向滑动则不拦截
                val isLateralSliding = abs(delayX) > abs(delayY)
                intercepted = if (isLateralSliding) {
                    log(TAG,"ACTION_MOVE   正在横向滑动  不拦截事件")
                    false
                } else {
                    log(TAG,"ACTION_MOVE   未横向滑动  拦截事件  delayX=$delayX  delayY=$delayY")
                    mInterceptCallback?.invoke() ?: true
                }
            }
            MotionEvent.ACTION_UP -> {
                //up时不拦截,这里拦截的话,子类就收不到onClick事件了
                intercepted = false
            }
            else -> {
            }
        }

        mLastXIntercept = x
        mLastYIntercept = y

        log(TAG,"最终结果 $intercepted")
        return intercepted
    }

    /**
     * 子类需要事件,则返回为false就好
     */
    fun setInterceptTouchCallback(callback: () -> Boolean) {
        mInterceptCallback = callback
    }

}