package com.xfhy.allinone

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.xfhy.allinone.jni.JNIMainActivity
import com.xfhy.allinone.opensource.OpenSourceActivity
import com.xfhy.allinone.smali.SmaliActivity
import kotlinx.android.synthetic.main.activity_main.*
import org.jetbrains.anko.startActivity

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        btnGoJni.setOnClickListener {
            startActivity<JNIMainActivity>()
        }
        btnGoSmali.setOnClickListener {
            startActivity<SmaliActivity>()
        }
        btnGoOpenSource.setOnClickListener {
            startActivity<OpenSourceActivity>()
        }
    }
}
