package com.xfhy.allinone.opensource.glide

import com.bumptech.glide.load.Options
import com.bumptech.glide.load.model.GlideUrl
import com.bumptech.glide.load.model.ModelLoader
import com.bumptech.glide.load.model.ModelLoaderFactory
import com.bumptech.glide.load.model.MultiModelLoaderFactory
import okhttp3.OkHttpClient
import java.io.InputStream

/**
 * @author : xfhy
 * Create time : 2020/12/3 7:16 AM
 * Description :
 */
class OkHttpGlideUrlLoader(private val okHttpClient: OkHttpClient) :
    ModelLoader<GlideUrl, InputStream> {

    class Factory(private var okHttpClient: OkHttpClient? = null) :
        ModelLoaderFactory<GlideUrl, InputStream> {

        @Synchronized
        private fun getOkHttpClient(): OkHttpClient {
            if (okHttpClient == null) {
                okHttpClient = OkHttpClient()
            }
            return okHttpClient as OkHttpClient
        }

        override fun build(multiFactory: MultiModelLoaderFactory): ModelLoader<GlideUrl, InputStream> {
            return OkHttpGlideUrlLoader(getOkHttpClient())
        }

        override fun teardown() {
            // Do nothing, this instance doesn't own the client.
        }
    }

    override fun buildLoadData(
        model: GlideUrl,
        width: Int,
        height: Int,
        options: Options
    ): ModelLoader.LoadData<InputStream>? {
        return ModelLoader.LoadData<InputStream>(model, OkHttpFetcher(okHttpClient, model))
    }

    override fun handles(model: GlideUrl): Boolean = true
}