package com.xfhy.allinone.actual

import android.os.Bundle
import com.xfhy.allinone.R
import com.xfhy.library.basekit.activity.TitleBarActivity
import kotlinx.android.synthetic.main.activity_actual_main.*
import org.jetbrains.anko.startActivity

/**
 * @author : xfhy
 * Create time : 2021/4/30 07:21
 * Description : 实战问题
 */
class ActualMainActivity : TitleBarActivity() {

    override fun getThisTitle() = "实战问题"

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_actual_main)

        btnGoReleaseLog.setOnClickListener {
            startActivity<ReleaseLogActivity>()
        }
    }
}
