package com.xfhy.allinone

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import com.xfhy.allinone.jni.JNIMainActivity
import com.xfhy.allinone.viewopt.ViewOptActivity
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

    fun goViewOpt(view: View) {
        startActivity<ViewOptActivity>()
    }
}
