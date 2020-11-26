package com.xfhy.allinone.opensource.glide

import android.os.Bundle
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import com.xfhy.allinone.R
import com.xfhy.library.basekit.activity.TitleBarActivity
import kotlinx.android.synthetic.main.activity_glide.*

/**
 * Glide解析
 */
class GlideActivity : TitleBarActivity() {
    override fun getThisTitle(): CharSequence {
        return "Glide"
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_glide)

        btnLoadImage.setOnClickListener {
            val requestOptions = RequestOptions().placeholder(R.drawable.splash_bg)
            Glide.with(this)
                .load("http://guolin.tech/book.png")
                .apply(requestOptions)
                .into(image_view)
        }
    }
}