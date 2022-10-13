package com.xfhy.allinone.actual.idlehandler

import android.os.Bundle
import android.view.View
import com.xfhy.allinone.R
import com.xfhy.library.basekit.activity.TitleBarActivity

/**
 * @author : xfhy
 * Create time : 2022/10/13
 * Description : 监控IdleHandler耗时问题
 * 把MessageQueue这个idlelist替换成自己的，重写add方法，添加进来的IdleHandler 给它包装一下，包装的那个类在执行queueIdle 时进行计时，这样添加进来的每个IdleHandler在执行的时候我们都能拿到其queueIdle 的执行时间。
 */
class WatchIdleHandlerActivity : TitleBarActivity() {

    override fun getThisTitle() = "监控IdleHandler耗时问题"

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_watch_idle_handler)
    }

    fun startDetection(view: View) {}
    fun startTimeConsuming(view: View) {}

}
