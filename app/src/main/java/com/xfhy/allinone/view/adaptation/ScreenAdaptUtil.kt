package com.xfhy.allinone.view.adaptation

import android.app.Activity
import android.app.Application
import android.content.ComponentCallbacks
import android.content.res.Configuration

/**
 * @author : xfhy
 * Create time : 2021/5/1 7:20 AM
 * Description : 屏幕适配
 */
object ScreenAdaptUtil {

    //原始的density和ScaleDensity
    private var nonCompatDensity = 0f
    private var nonCompatScaleDensity = 0f

    //如果DisplayMetrics#scaledDensity和DisplayMetrics#density设置为同样的值，
    // 从而某些用户在系统中修改了字体大小失效了，但是我们还不能直接用原始的scaledDensity，
    // 直接用的话可能导致某些文字超过显示区域，因此我们可以通过计算之前scaledDensity和density的比获得现在的scaledDensity

    fun setCustomDensity(activity: Activity, application: Application) {
        val appDisplayMetrics = application.resources.displayMetrics

        //监听用户动态修改了字体大小   从而跟进设置一下nonCompatScaleDensity
        if (nonCompatDensity == 0f) {
            nonCompatDensity = appDisplayMetrics.density
            nonCompatScaleDensity = appDisplayMetrics.scaledDensity
            application.registerComponentCallbacks(object : ComponentCallbacks {
                override fun onConfigurationChanged(newConfig: Configuration) {
                    if (newConfig.fontScale > 0) {
                        nonCompatScaleDensity = application.resources.displayMetrics.scaledDensity
                    }
                }

                override fun onLowMemory() {
                }
            })
        }

        val targetDensity = appDisplayMetrics.widthPixels / 360f
        val targetScaleDensity = targetDensity * (nonCompatScaleDensity / nonCompatDensity)
        //dpi = density*160
        val targetDensityDpi = (160 * targetDensity).toInt()

        appDisplayMetrics.density = targetDensity
        //与字体大小有关
        appDisplayMetrics.scaledDensity = targetScaleDensity
        appDisplayMetrics.densityDpi = targetDensityDpi

        val activityDisplayMetrics = activity.resources.displayMetrics
        activityDisplayMetrics.density = targetDensity
        activityDisplayMetrics.scaledDensity = targetScaleDensity
        activityDisplayMetrics.densityDpi = targetDensityDpi
    }

}