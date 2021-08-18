package com.xfhy.allinone.actual.plugin

import android.os.Bundle
import com.xfhy.allinone.R
import com.xfhy.library.basekit.activity.TitleBarActivity

/**
 * @author : xfhy
 * Create time : 2021/8/16 21:51
 * Description : 插件化 占坑Activity
 */
class StubActivity : TitleBarActivity() {


    override fun getThisTitle() = "插件化 占坑Activity"

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_plugin_stub)
    }

}
