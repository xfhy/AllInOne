package com.xfhy.nativelib

/**
 * @author : xfhy
 * Create time : 2023/2/10 7:56
 * Description :
 */
class MonitorMalloc {

    companion object {
        init {
            System.loadLibrary("nativelib")
        }
    }

    external fun startMonitor()

}