package com.xfhy.allinone.scroll.rv.view

import android.annotation.SuppressLint
import android.content.Context
import android.util.AttributeSet
import android.view.MotionEvent
import androidx.recyclerview.widget.RecyclerView

/**
 * @author : xfhy
 * Create time : 2021/2/7 10:50
 * Description :
 */
class ParentRecyclerView @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = 0
) : RecyclerView(context, attrs, defStyleAttr) {

    private var lastY = 0f
    var mGetChildCallback: (() -> ChildRecyclerView?)? = null

    private fun isScrollEnd(): Boolean {
        //RecyclerView.canScrollVertically(1)的值表示是否能向上滚动，false表示已经滚动到底部
        return !canScrollVertically(1)
    }

    @SuppressLint("ClickableViewAccessibility")
    override fun onTouchEvent(e: MotionEvent): Boolean {
        if (lastY == 0f) {
            lastY = e.y
        }
        if (isScrollEnd()) {
            //如果父RecyclerView已经滑动到底部，需要让子RecyclerView滑动剩余的距离
            val childRecyclerView = mGetChildCallback?.invoke()
            childRecyclerView?.run {
                val deltaY = (lastY - e.y).toInt()
                if (deltaY != 0) {
                    scrollBy(0, deltaY)
                }
            }
        }
        lastY = e.y
        return try {
            super.onTouchEvent(e)
        } catch (e: Exception) {
            e.printStackTrace()
            false
        }
    }

}