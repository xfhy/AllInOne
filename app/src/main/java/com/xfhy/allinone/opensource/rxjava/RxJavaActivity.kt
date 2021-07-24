package com.xfhy.allinone.opensource.rxjava

import android.os.Bundle
import com.xfhy.allinone.R
import com.xfhy.library.basekit.activity.TitleBarActivity

/**
 * RxJava
 */
class RxJavaActivity : TitleBarActivity() {

    override fun getThisTitle() = "RxJava Test"

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_rxjava)

        initView()
    }

    private fun initView() {

    }
}