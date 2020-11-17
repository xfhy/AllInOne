package com.xfhy.library.basekit.activity

import android.content.Context
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.xfhy.library.common.AppManager
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.MainScope
import kotlinx.coroutines.cancel

/**
 * @author xfhy
 * time create at 2018/1/27 9:09
 * description Activity基类
 */
abstract class BaseActivity : AppCompatActivity(), CoroutineScope by MainScope() {
    val mContext: Context by lazy { this }   //lazy只能用于val

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        AppManager.instance.addActivity(this)
    }

    override fun onDestroy() {
        super.onDestroy()
        //取消协程
        cancel()
        AppManager.instance.finishActivity(this)
    }

}