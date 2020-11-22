package com.xfhy.allinone.opensource

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.xfhy.allinone.R
import com.xfhy.allinone.opensource.okhttp.OkHttpActivity
import com.xfhy.allinone.opensource.retrofit.RetrofitActivity
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
        btnGoRetrofit.setOnClickListener {
            startActivity<RetrofitActivity>()
        }
        btnGoGlide.setOnClickListener {
            startActivity<OkHttpActivity>()
        }
        btnGoGreenDao.setOnClickListener {
            startActivity<OkHttpActivity>()
        }
        btnGoRxJava.setOnClickListener {
            startActivity<OkHttpActivity>()
        }
        btnLeakCanary.setOnClickListener {
            startActivity<OkHttpActivity>()
        }
        btnGoButterknife.setOnClickListener {
            startActivity<OkHttpActivity>()
        }
        btnGoEventBus.setOnClickListener {
            startActivity<OkHttpActivity>()
        }
    }
}