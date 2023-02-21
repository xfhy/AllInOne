package com.xfhy.library.service

import android.app.IntentService
import android.content.Intent
import com.xfhy.library.data.net.OkHttpUtils

/**
 * 2018年10月29日15:13:21
 * @author xfhy
 * 初始化一些第三方库
 *
 * 在Application中start
 */
class InitIntentService : IntentService("InitIntentService") {

    override fun onHandleIntent(intent: Intent?) {
        OkHttpUtils.initOkHttp(applicationContext)

        //ARouter
        /*if (BuildConfig.DEBUG) {  // 这两行必须写在init之前，否则这些配置在init过程中将无效
            ARouter.openLog()     // 打印日志
            ARouter.openDebug()   // 开启调试模式(如果在InstantRun模式下运行，必须开启调试模式！线上版本需要关闭,否则有安全风险)
        }
        ARouter.init(application)   // 尽可能早，推荐在Application中初始化*/
    }
}
