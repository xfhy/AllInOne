package com.xfhy.allinone.scroll

import android.os.Bundle
import com.xfhy.allinone.R
import com.xfhy.allinone.scroll.rv.RvContainsVpActivity
import com.xfhy.library.basekit.activity.TitleBarActivity
import kotlinx.android.synthetic.main.activity_scroll_main.*
import org.jetbrains.anko.startActivity

/**
 * View 滑动
 */
class ScrollMainActivity : TitleBarActivity() {

    override fun getThisTitle() = "Scroll"

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_scroll_main)

        btnGoSimpleScroll.setOnClickListener { startActivity<SimpleScrollActivity>() }
        btnGoRvContainsVP.setOnClickListener { startActivity<RvContainsVpActivity>() }

    }
}
