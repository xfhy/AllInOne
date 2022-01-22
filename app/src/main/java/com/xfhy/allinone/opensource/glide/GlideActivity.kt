package com.xfhy.allinone.opensource.glide

import android.graphics.drawable.Drawable
import android.os.Bundle
import android.view.View
import android.widget.ImageView
import android.widget.Toast
import com.bumptech.glide.Glide
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.bumptech.glide.request.RequestOptions
import com.bumptech.glide.request.target.CustomViewTarget
import com.bumptech.glide.request.transition.Transition
import com.xfhy.allinone.App
import com.xfhy.allinone.R
import com.xfhy.library.basekit.activity.TitleBarActivity
import kotlinx.android.synthetic.main.activity_glide.*
import kotlin.concurrent.thread

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
            simpleTest()
//            targetTest()
//            downloadImage()
        }
    }

    /**
     * 下载图片
     */
    private fun downloadImage() {
        thread {
            try {
                val url = "http://www.guolin.tech/book.png"
                val context = App.getAppContext()
                val target = Glide.with(context).asFile().load(url).submit()
                val file = target.get()
                runOnUiThread {
                    Toast.makeText(context, file?.path, Toast.LENGTH_LONG).show()
                }
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }
    }

    private fun targetTest() {
        val customViewTarget =
            object : CustomViewTarget<ImageView, Drawable>(image_view) {
                override fun onLoadFailed(errorDrawable: Drawable?) {
                }

                override fun onResourceCleared(placeholder: Drawable?) {
                }

                override fun onResourceReady(
                    resource: Drawable,
                    transition: Transition<in Drawable>?
                ) {
                    image_view.setImageDrawable(resource)
                }
            }
        Glide.with(this).load("http://guolin.tech/book.png").into(customViewTarget)
    }

    private fun simpleTest() {
        val requestOptions = RequestOptions()
            .placeholder(R.drawable.splash_bg)
            .diskCacheStrategy(DiskCacheStrategy.NONE)
            .error(R.mipmap.ic_launcher)
            .override(200, 200)
            .skipMemoryCache(true)
        val url = "http://guolin.tech/book.png"

        ProgressInterceptor.addProcessListener(url) { progress ->
            glideProgressBar.progress = progress
        }

        Glide.with(this)
            .load(url)
            //不管是不是动态图,都加载成静态图
            //.asBitmap()
            //.load("http://guolin.tech/test.gif")
            .apply(requestOptions)
            .into(object : CustomViewTarget<ImageView, Drawable>(image_view) {
                override fun onLoadFailed(errorDrawable: Drawable?) {
                }

                override fun onResourceCleared(placeholder: Drawable?) {

                }

                override fun onStart() {
                    super.onStart()
                    glideProgressBar.visibility = View.VISIBLE
                }

                override fun onResourceReady(
                    resource: Drawable,
                    transition: Transition<in Drawable>?
                ) {
                    image_view.setImageDrawable(resource)
                    ProgressInterceptor.removeListener(url)
                }
            })
    }
}