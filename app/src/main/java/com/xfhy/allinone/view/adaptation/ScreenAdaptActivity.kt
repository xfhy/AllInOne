package com.xfhy.allinone.view.adaptation

import android.content.res.Resources
import android.os.Bundle
import com.xfhy.allinone.R
import com.xfhy.library.basekit.activity.TitleBarActivity
import com.xfhy.library.ext.toast
import kotlinx.android.synthetic.main.activity_screen_adapt.*

/**
 * @author : xfhy
 * Create time : 2021/5/1 07:16
 * Description : 屏幕适配
 * 今日头条 一种极低成本的Android屏幕适配方式: https://mp.weixin.qq.com/s/d9QCoBP6kV9VSWvVldVVwA
 * px=dp*density
 * 根据设计图的宽或高,修改density,比如设备宽是1080,设计图宽是360,那么将density修改为px/dp=1080/360=3
 */
class ScreenAdaptActivity : TitleBarActivity() {

    override fun getThisTitle() = "屏幕适配"

    override fun onCreate(savedInstanceState: Bundle?) {
        //适配一下
        //ScreenAdaptUtil.setCustomDensity(this, App.getAppContext() as Application)

        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_screen_adapt)

        tv_toast.setOnClickListener {
            toast("哈哈哈哈哈哈哈")
        }
    }

    override fun getResources(): Resources {
        //假设 设计图宽度为360
        return ScreenAdaptUtil.adaptWidth(super.getResources(), 360)
    }

}
