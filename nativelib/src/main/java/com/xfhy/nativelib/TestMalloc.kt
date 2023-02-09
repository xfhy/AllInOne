package com.xfhy.nativelib

/**
 * @author : xfhy
 * Create time : 2023/2/9 21:31
 * Description :
 */
class TestMalloc {

    companion object {
        // Used to load the 'nativelib' library on application startup.
        init {
            System.loadLibrary("testmalloc")
        }
    }

    external fun testMalloc():IntArray

}