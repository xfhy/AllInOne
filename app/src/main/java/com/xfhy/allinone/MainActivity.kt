package com.xfhy.allinone

import android.os.Bundle
import com.xfhy.allinone.image.ImageHomeActivity
import com.xfhy.allinone.ipc.IpcMainActivity
import com.xfhy.allinone.jni.JNIMainActivity
import com.xfhy.allinone.opensource.OpenSourceActivity
import com.xfhy.allinone.scroll.ScrollMainActivity
import com.xfhy.allinone.smali.SmaliActivity
import com.xfhy.library.basekit.activity.TitleBarActivity
import kotlinx.android.synthetic.main.activity_main.*
import org.jetbrains.anko.startActivity

class MainActivity : TitleBarActivity() {

    override fun getThisTitle() = "Demo All In One"

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        setLeftTvVisible(false)

        btnGoJni.setOnClickListener {
            startActivity<JNIMainActivity>()
        }
        btnGoSmali.setOnClickListener {
            startActivity<SmaliActivity>()
        }
        btnGoOpenSource.setOnClickListener {
            startActivity<OpenSourceActivity>()
        }
        btnGoIpc.setOnClickListener {
            startActivity<IpcMainActivity>()
        }
        btnGoScroll.setOnClickListener {
            startActivity<ScrollMainActivity>()
        }
        btnGoImageHome.setOnClickListener {
            startActivity<ImageHomeActivity>()
        }
    }
}
