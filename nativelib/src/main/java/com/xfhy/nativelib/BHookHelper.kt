package com.xfhy.nativelib

import com.bytedance.android.bytehook.ByteHook

/**
 * @author : xfhy
 * Create time : 2023/2/14 21:29
 * Description :
 *
 * bhook api : https://github.com/bytedance/bhook/blob/c0a83d41e0820506068031149f5f0057cde0abfe/doc/native_manual.zh-CN.md
 *
 */
object BHookHelper {

    @Volatile
    var isInitBhook = false

    @Synchronized
    fun initBHook() {
        if (isInitBhook) {
            return
        }
        isInitBhook = true
        ByteHook.setDebug(true)
        ByteHook.init()
    }

}