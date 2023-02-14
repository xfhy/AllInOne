package com.xfhy.nativelib

import com.bytedance.android.bytehook.ByteHook




/**
 * @author : xfhy
 * Create time : 2023/2/14 21:29
 * Description :
 */
object BHookHelper {

    @Synchronized
    fun initBHook() {
        ByteHook.init()
    }

}