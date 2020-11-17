package com.xfhy.library.common

import android.annotation.SuppressLint
import android.app.Activity
import android.app.Application
import android.content.Context
import android.os.Bundle


/**
 * @author xfhy
 * time create at 2018/2/1 20:24
 * description Application基类
 */
open class BaseApplication : Application() {

    companion object {
        @SuppressLint("StaticFieldLeak")
        lateinit var context: Context
    }


    override fun onCreate() {
        super.onCreate()

        context = this
    }

}