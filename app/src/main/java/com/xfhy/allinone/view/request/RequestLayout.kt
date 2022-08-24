package com.xfhy.allinone.view.request

import android.os.Bundle
import androidx.lifecycle.lifecycleScope
import com.xfhy.allinone.R
import com.xfhy.library.basekit.activity.TitleBarActivity
import com.xfhy.library.ext.dp
import com.xfhy.library.ext.log
import kotlinx.android.synthetic.main.activity_request_layout.*
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

/**
 * @author : xfhy
 * Create time : 2022/8/25 6:44 上午
 * Description : requestLayout学习
 */
class RequestLayoutActivity : TitleBarActivity() {

    override fun getThisTitle() = "requestLayout"

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_request_layout)

        lifecycleScope.launch {
            delay(5000)

            val layoutParams = tv_name.layoutParams
            layoutParams.width = 20.dp
            tv_name.layoutParams = layoutParams
            log("requestLayout")
        }

    }

}