package com.xfhy.allinone.scroll.rv.view

import android.content.Context
import android.util.AttributeSet
import androidx.recyclerview.widget.RecyclerView

/**
 * @author : xfhy
 * Create time : 2021/2/7 10:50
 * Description :
 */
const val TAG = "xfhy_scroll"

class ChildRecyclerView @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = 0
) : RecyclerView(context, attrs, defStyleAttr) {

    /**
     * 是否已经滑动到顶部
     */
    fun isScrollTop(): Boolean? {
        //RecyclerView#canScrollVertically(-1)的值表示能否向下滑动   false表示已经滑动到顶部
        return !canScrollVertically(-1)
    }

}