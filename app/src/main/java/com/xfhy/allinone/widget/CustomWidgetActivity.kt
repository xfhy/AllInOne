package com.xfhy.allinone.widget

import android.os.Bundle
import com.xfhy.allinone.R
import com.xfhy.library.basekit.activity.TitleBarActivity

/**
 * @author : xfhy
 * Create time : 2022年04月26日10:18:19
 * Description : 自定义View
 */
class CustomWidgetActivity : TitleBarActivity() {

    override fun getThisTitle(): CharSequence = "自定义View"

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_custom_widget)
    }

}