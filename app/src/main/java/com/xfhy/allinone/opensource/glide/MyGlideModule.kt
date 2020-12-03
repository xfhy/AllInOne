package com.xfhy.allinone.opensource.glide

import android.content.Context
import com.bumptech.glide.Glide
import com.bumptech.glide.GlideBuilder
import com.bumptech.glide.Registry
import com.bumptech.glide.load.model.GlideUrl
import com.bumptech.glide.module.GlideModule
import com.xfhy.library.ext.log
import okhttp3.OkHttpClient
import java.io.InputStream


/**
 * @author : xfhy
 * Create time : 2020/12/3 7:27 AM
 * Description :  Glide自定义模块
 */
class MyGlideModule : GlideModule {

    override fun applyOptions(context: Context, builder: GlideBuilder) {

    }

    override fun registerComponents(context: Context, glide: Glide, registry: Registry) {
        log("xfhy_glide", "registerComponents 来了来了")
        val okHttpClient = OkHttpClient.Builder()
            .addInterceptor(ProgressInterceptor())
            .build()
        registry.replace(
            GlideUrl::class.java,
            InputStream::class.java,
            OkHttpGlideUrlLoader.Factory(okHttpClient)
        )
    }
}