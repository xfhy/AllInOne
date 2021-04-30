package com.xfhy.allinone.opensource.glide

import com.xfhy.library.ext.log
import okhttp3.Interceptor
import okhttp3.MediaType
import okhttp3.Response
import okhttp3.ResponseBody
import okio.Buffer
import okio.BufferedSource
import okio.ForwardingSource
import okio.Source

/**
 * @author : xfhy
 * Create time : 2020/12/4 6:45 AM
 * Description : Glide进度 拦截器
 */
class ProgressInterceptor : Interceptor {

    private var progressListener: (Int) -> Unit = {}

    companion object {
        private val LISTENER_MAP = hashMapOf<String, Any>()

        fun addProcessListener(url: String, listener: (Int) -> Unit) {
            LISTENER_MAP[url] = listener
        }

        fun removeListener(url: String) {
            LISTENER_MAP.remove(url)
        }
    }

    override fun intercept(chain: Interceptor.Chain): Response {
        val request = chain.request()
        val response = chain.proceed(request)
        log("xfhy_glide", "ProgressInterceptor  intercept")
        val url = request.url().toString()
        val body = response.body()
        return response.newBuilder().body(body?.let {
            ProgressResponseBody(
                LISTENER_MAP[url] as? (Int) -> Unit ?: {},
                it
            )
        }).build()
    }

    class ProgressResponseBody(
        private val listener: (Int) -> Unit,
        private val responseBody: ResponseBody
    ) : ResponseBody() {
        private var bufferedSource: BufferedSource? = null

        override fun contentLength(): Long = responseBody.contentLength()

        override fun contentType(): MediaType? = responseBody.contentType()

        override fun source(): BufferedSource {
            if (bufferedSource == null) {
                bufferedSource = ProcessSource(responseBody.source(), responseBody, listener).delegate as BufferedSource?
            }
            return bufferedSource!!
        }
    }

    class ProcessSource(
        delegate: Source,
        private val responseBody: ResponseBody,
        private val listener: (Int) -> Unit
    ) : ForwardingSource(delegate) {
        private var totalBytesRead: Long = 0

        private var currentProgress = 0

        override fun read(sink: Buffer, byteCount: Long): Long {
            val bytesRead = super.read(sink, byteCount)
            val fullLength: Long = responseBody.contentLength()
            if (bytesRead == -1L) {
                totalBytesRead = fullLength
            } else {
                totalBytesRead += bytesRead
            }
            val progress = (100f * totalBytesRead / fullLength).toInt()
            log("xfhy_glide", "download progress is $progress")
            if (progress != currentProgress) {
                listener.invoke(progress)
            }
            currentProgress = progress
            return bytesRead
        }

    }

}