package com.xfhy.allinone.view.adaptation

import android.app.Activity
import android.app.Application
import android.content.res.Resources
import android.util.DisplayMetrics
import com.xfhy.allinone.App
import java.lang.reflect.Field
import java.util.*

/**
 * @author : xfhy
 * Create time : 2021/5/1 7:20 AM
 * Description : 屏幕适配
 */
object ScreenAdaptUtil {

    //-----------------------今日头条方案 start-----------------------
    //一种极低成本的Android屏幕适配方式   https://mp.weixin.qq.com/s/d9QCoBP6kV9VSWvVldVVwA
    //原始的density和ScaleDensity
    private var nonCompatDensity = 0f
    private var nonCompatScaleDensity = 0f

    //如果DisplayMetrics#scaledDensity和DisplayMetrics#density设置为同样的值，
    // 从而某些用户在系统中修改了字体大小失效了，但是我们还不能直接用原始的scaledDensity，
    // 直接用的话可能导致某些文字超过显示区域，因此我们可以通过计算之前scaledDensity和density的比获得现在的scaledDensity

    fun setCustomDensity(activity: Activity, application: Application) {
        val appDisplayMetrics = application.resources.displayMetrics

        //监听用户动态修改了字体大小   从而跟进设置一下nonCompatScaleDensity
        //使用Resources.getSystem()的话,就不用监听,能感知到
        /*if (nonCompatDensity == 0f) {
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
        }*/
        //假设  设计图宽度为360
        val targetDensity = appDisplayMetrics.widthPixels / 360f
        val targetScaleDensity = targetDensity * (Resources.getSystem().displayMetrics.scaledDensity / Resources.getSystem().displayMetrics.density)
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
    //-----------------------今日头条方案 end-----------------------

    //----------------------柯基方案  start -----------------------
    //Android 屏幕适配终结者   https://blankj.com/2018/12/18/android-adapt-screen-killer/
    fun adaptWidth(resources: Resources, designWidth: Int): Resources {
        val newXdpi = resources.displayMetrics.widthPixels * 72f / designWidth
        applyDisplayMetrics(resources, newXdpi)
        return resources
    }

    private fun applyDisplayMetrics(resources: Resources, newXdpi: Float) {
        resources.displayMetrics.xdpi = newXdpi
        App.getAppContext().resources.displayMetrics.xdpi = newXdpi
        applyOtherDisplayMetrics(resources, newXdpi);
    }

    private var sMetricsFields: MutableList<Field?>? = null

    private fun applyOtherDisplayMetrics(resources: Resources, newXdpi: Float) {
        if (sMetricsFields == null) {
            sMetricsFields = ArrayList()
            var resCls: Class<*>? = resources.javaClass
            var declaredFields = resCls?.declaredFields
            while (declaredFields != null && declaredFields.isNotEmpty()) {
                for (field in declaredFields) {
                    if (field.type.isAssignableFrom(DisplayMetrics::class.java)) {
                        field.isAccessible = true
                        val tmpDm = getMetricsFromField(resources, field)
                        if (tmpDm != null) {
                            sMetricsFields?.add(field)
                            tmpDm.xdpi = newXdpi
                        }
                    }
                }
                resCls = resCls?.superclass
                declaredFields = if (resCls != null) {
                    resCls.declaredFields
                } else {
                    break
                }
            }
        } else {
            applyMetricsFields(resources, newXdpi)
        }
    }

    private fun applyMetricsFields(resources: Resources, newXdpi: Float) {
        sMetricsFields?.let {
            it.forEach { field ->
                if (field != null) {
                    try {
                        val dm = field[resources] as DisplayMetrics
                        dm.xdpi = newXdpi
                    } catch (e: Exception) {
                        e.printStackTrace()
                    }
                }

            }
        }
    }

    private fun getMetricsFromField(resources: Resources, field: Field): DisplayMetrics? {
        return try {
            field[resources] as DisplayMetrics
        } catch (ignore: Exception) {
            null
        }
    }
    //----------------------柯基方案  end -----------------------
}