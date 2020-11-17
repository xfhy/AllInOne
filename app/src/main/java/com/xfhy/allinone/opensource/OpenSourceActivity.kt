package com.xfhy.allinone.opensource

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.xfhy.allinone.R
import com.xfhy.allinone.opensource.okhttp.OkHttpActivity
import kotlinx.android.synthetic.main.activity_open_source.*
import org.jetbrains.anko.startActivity

/**
 * 常见开源库源码赏析
 */
class OpenSourceActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_open_source)

        initView()

    }

    private fun initView() {
        btnGoOkhttp.setOnClickListener {
            startActivity<OkHttpActivity>()
        }
    }
}