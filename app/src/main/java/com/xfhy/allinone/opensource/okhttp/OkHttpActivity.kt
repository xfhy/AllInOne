package com.xfhy.allinone.opensource.okhttp

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.xfhy.allinone.R
import com.xfhy.library.ext.log
import com.xfhy.library.ext.logString
import kotlinx.android.synthetic.main.activity_ok_http.*
import kotlinx.coroutines.*
import okhttp3.*
import java.io.IOException

class OkHttpActivity : AppCompatActivity() {

    object OkHttpManager {
        val okHttpClient = OkHttpClient()
    }

    private val mainScope = MainScope()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_ok_http)

        btnTestOkSync.setOnClickListener {
            testSync()
        }

        btnTestOkAsync.setOnClickListener {
            testAsync()
        }

    }

    private fun testAsync() {
        val request = Request.Builder().url("https://www.baidu.com/").build()
        OkHttpManager.okHttpClient.newCall(request).enqueue(object : Callback {
            override fun onFailure(call: Call, e: IOException) {
                log(msg = "失败了")
            }

            override fun onResponse(call: Call, response: Response) {
                val responseBody = response.body()
                responseBody?.string().logString()
            }
        })

    }

    private fun testSync() {
        mainScope.launch {
            val async = async(Dispatchers.IO) {
                val request = Request.Builder().url("https://www.baidu.com/").build()
                val response = OkHttpManager.okHttpClient.newCall(request).execute()
                val responseBody = response.body()
                responseBody?.string().logString()
            }
            async.await()
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        mainScope.cancel()
    }

}