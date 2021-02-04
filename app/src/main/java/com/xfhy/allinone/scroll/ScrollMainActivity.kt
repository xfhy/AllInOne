package com.xfhy.allinone.scroll

import android.os.Bundle
import com.xfhy.allinone.R
import com.xfhy.library.basekit.activity.TitleBarActivity

/**
 * View 滑动
 */
class ScrollMainActivity : TitleBarActivity() {

    override fun getThisTitle() = "Scroll"

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_scroll_main)
    }
}
