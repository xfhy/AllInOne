package com.xfhy.allinone.actual.hotfix

import android.os.Bundle
import android.view.View
import com.xfhy.allinone.R
import com.xfhy.library.basekit.activity.TitleBarActivity

/**
 * @author : xfhy
 * Create time : 2021/8/27 07:53
 * Description : 热更新
 * 源码目录: https://www.androidos.net.cn/android/8.0.0_r4/xref/libcore/dalvik/src/main/java/dalvik/system
 */
class HotFixActivity : TitleBarActivity() {

    override fun getThisTitle() = "热更新"

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_hotfix)
    }

    fun callTestMethod(view: View) {
        //到达这个页面在加载补丁的话,HotFixTest已经被PathClassLoader加载好了,有缓存,此时即使热修复了也不能调用到补丁包里面的HotFixTest.
        //调试的时候注意一下
        HotFixTest.showSomething()
    }

    fun loadPatch(view: View) {
        //已在Application.onCreate中调用了  HotFixUtil.loadPatch()
    }

    fun restartApp(view: View) {

    }


}
