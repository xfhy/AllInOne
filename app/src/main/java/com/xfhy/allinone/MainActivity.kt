package com.xfhy.allinone

import android.os.Bundle
import com.xfhy.allinone.ipc.aidl.AidlActivity
import com.xfhy.allinone.ipc.binder.BinderActivity
import com.xfhy.allinone.ipc.messenger.MessengerActivity
import com.xfhy.allinone.jni.JNIMainActivity
import com.xfhy.allinone.opensource.OpenSourceActivity
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
        btnGoAidl.setOnClickListener {
            startActivity<AidlActivity>()
        }
        btnGoMessenger.setOnClickListener {
            startActivity<MessengerActivity>()
        }
        btnGoBinder.setOnClickListener {
            startActivity<BinderActivity>()
        }
    }
}
