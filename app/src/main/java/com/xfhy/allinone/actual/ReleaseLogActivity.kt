package com.xfhy.allinone.actual

import android.os.Bundle
import android.os.SystemClock
import android.util.Log
import android.widget.Toast
import com.xfhy.allinone.BuildConfig
import com.xfhy.allinone.R
import com.xfhy.library.basekit.activity.TitleBarActivity
import kotlinx.android.synthetic.main.activity_release_log.*

/**
 * @author : xfhy
 * Create time : 2021/4/30 06:44
 * Description : 线上release包开启log
 *
 * 点击5次进入日志收集模式  客服引导用户开启这个,然后才能收集那个用户的现场操作日志.
 */
class ReleaseLogActivity : TitleBarActivity() {

    override fun getThisTitle() = "线上release包开启log"

    //几击事件，数组就是几，双击事件就是2
    private val mHits = LongArray(5)


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_release_log)

        TempLogUtil.d("test  打印日志")
        TempLogUtil.d("test  打印日志")

        ivCenterImg.setOnClickListener {
            System.arraycopy(mHits, 1, mHits, 0, mHits.size - 1)
            mHits[mHits.size - 1] = SystemClock.uptimeMillis()
            if (mHits[mHits.size - 1] - mHits[0] < 4000) {
                //响应了一个三击事件
                Toast.makeText(applicationContext, "已开启日志收集模式", Toast.LENGTH_LONG).show();
                TempLogUtil.turnOnLog()
            }
        }
    }
}

object TempLogUtil {
    //通常情况下我们会使用 BuildConfig.DEBUG 来作为是否要打印日志的开关。但是使用这个变量具有一定的局限性。
    // 比如现场突然发现一个异常现象，而我们需要现场抓取异常的日志信息加以分析。因为是 release 版本，
    // 所有不会有任何 log 信息被打印。因此这个开关的设置最好具有一定的灵活性，
    // 比如可以再加一层 System Property 的设置
    //我在API 28上测试时,退出app,这个系统属性就失效了,下次进入就获取不到了
    private var debug = getRealSwitch()

    private fun getRealSwitch(): Boolean {
        val switch = System.getProperty("persist.xfhy.log", "")
        debug = if (switch == "true") {
            true
        } else {
            BuildConfig.DEBUG
        }
        return debug
    }

    fun turnOnLog() {
        debug = true
        System.setProperty("persist.xfhy.log", "true")
    }

    fun d(msg: String) {
        if (debug) {
            Log.d("xfhy_test", msg)
        }
    }
}