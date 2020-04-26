package com.xfhy.allinone

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.xfhy.allinone.jni.JNIMainActivity
import kotlinx.android.synthetic.main.activity_main.*
import org.jetbrains.anko.startActivity

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        btnGoJni.setOnClickListener {
            startActivity<JNIMainActivity>()
        }
    }
}
