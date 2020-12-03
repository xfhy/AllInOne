package com.xfhy.allinone.opensource.glide

import com.bumptech.glide.Priority
import com.bumptech.glide.load.DataSource
import com.bumptech.glide.load.data.DataFetcher
import com.bumptech.glide.load.model.GlideUrl
import com.bumptech.glide.util.ContentLengthInputStream
import com.xfhy.library.ext.log
import okhttp3.Call
import okhttp3.OkHttpClient
import okhttp3.Request
import okhttp3.ResponseBody
import java.io.FileInputStream
import java.io.IOException
import java.io.InputStream

/**
 * @author : xfhy
 * Create time : 2020/12/3 7:01 AM
 * Description : Glide网络请求
 */
class OkHttpFetcher(private val okHttpClient: OkHttpClient, private val url: GlideUrl) :
    DataFetcher<InputStream> {

    private var stream: InputStream? = null
    private var responseBody: ResponseBody? = null
    private var call: Call? = null

    @Volatile
    private var isCancelled = false

    override fun getDataClass(): Class<InputStream> = InputStream::class.java

    override fun cleanup() {
        try {
            stream?.close()
            responseBody?.close()
        } catch (e: IOException) {
            e.printStackTrace()
        }
    }

    override fun getDataSource(): DataSource = DataSource.REMOTE

    override fun cancel() {
        call?.cancel()
    }

    override fun loadData(priority: Priority, callback: DataFetcher.DataCallback<in InputStream>) {
        val requestBuilder = Request.Builder().url(url.toStringUrl())
        for (headerEntry in url.headers.entries) {
            requestBuilder.addHeader(headerEntry.key, headerEntry.value)
        }

        //this is a test header
        requestBuilder.addHeader("xfhy", "test")
        log("xfhy_glide", "添加测试header")

        val request = requestBuilder.build()
        if (isCancelled) {
            callback.onDataReady(null)
            return
        }
        call = okHttpClient.newCall(request)
        val response = call?.execute()
        val responseBody = response?.body()
        if (response?.isSuccessful != true || responseBody == null) {
            callback.onLoadFailed(IOException("Request failed with code: ${response?.code()}"))
        }
        stream = ContentLengthInputStream.obtain(
            responseBody?.byteStream() ?: FileInputStream(""),
            responseBody?.contentLength() ?: 0
        )
        callback.onDataReady(stream)
    }
}