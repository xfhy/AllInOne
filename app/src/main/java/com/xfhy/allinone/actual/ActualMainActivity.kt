package com.xfhy.allinone.actual

import android.os.Bundle
import android.view.View
import com.xfhy.allinone.R
import com.xfhy.allinone.actual.apt.AptActivity
import com.xfhy.allinone.actual.hotfix.HotFixActivity
import com.xfhy.allinone.actual.idlehandler.WatchIdleHandlerActivity
import com.xfhy.allinone.actual.plugin.PluginAppActivity
import com.xfhy.allinone.actual.syncbarrier.WatchSyncBarrierActivity
import com.xfhy.library.basekit.activity.TitleBarActivity
import kotlinx.android.synthetic.main.activity_actual_main.*
import org.jetbrains.anko.startActivity

/**
 * @author : xfhy
 * Create time : 2021/4/30 07:21
 * Description : 实战问题
 */
class ActualMainActivity : TitleBarActivity() {

    override fun getThisTitle() = "实战问题"

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_actual_main)

        btnGoReleaseLog.setOnClickListener {
            startActivity<ReleaseLogActivity>()
        }
    }

    fun pluginApkBtn(view: View) {
        startActivity<PluginAppActivity>()
    }

    fun goHotFix(view: View) {
        startActivity<HotFixActivity>()
    }

    fun goApt(view: View) {
        startActivity<AptActivity>()
    }

    fun watchIdleHandler(view: View) {
        startActivity<WatchIdleHandlerActivity>()
    }

    fun watchSyncBarrier(view: View) {
        startActivity<WatchSyncBarrierActivity>()
    }
}
