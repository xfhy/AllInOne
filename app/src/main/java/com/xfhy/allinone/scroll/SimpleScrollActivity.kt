package com.xfhy.allinone.scroll

import android.os.Bundle
import com.xfhy.allinone.R
import com.xfhy.library.basekit.activity.TitleBarActivity

/**
 * 简单滑动冲突
 */
class SimpleScrollActivity : TitleBarActivity() {

    override fun getThisTitle() = "Scroll"

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_simple_scroll)
    }
}
