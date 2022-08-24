package com.xfhy.allinone.view.request

import android.content.Context
import android.util.AttributeSet
import android.widget.FrameLayout
import com.xfhy.library.ext.log

/**
 * @author : xfhy
 * Create time : 2022/8/25 7:18 上午
 * Description : 测试RequestLayout
 */
class OtherFrameLayout @JvmOverloads constructor(context: Context, attrs: AttributeSet? = null, defStyleAttr: Int = 0) :
    FrameLayout(context, attrs, defStyleAttr) {

    override fun onLayout(changed: Boolean, left: Int, top: Int, right: Int, bottom: Int) {
        super.onLayout(changed, left, top, right, bottom)
        log("OtherFrameLayout tag=$tag onLayout")
    }

    override fun onMeasure(widthMeasureSpec: Int, heightMeasureSpec: Int) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec)
        log("OtherFrameLayout tag=$tag onMeasure")
    }

    override fun requestLayout() {
        super.requestLayout()
    }

}