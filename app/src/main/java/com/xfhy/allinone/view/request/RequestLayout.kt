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
 *
 * 结论: 当修改某个View的宽度或者高度,View树中由它通向View树根节点的这条线上的View,都会onMeasure,onLayout,onDraw重绘.
 * 因为沿着这个View往View树上面走,这条线上的View都会打上PFLAG_FORCE_LAYOUT标记,到下个VSYNC信号到来时,会走View的三大流程.
 *
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