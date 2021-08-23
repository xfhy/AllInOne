package com.xfhy.allinone.actual.plugin

import android.content.Intent
import android.os.Bundle
import android.view.View
import androidx.lifecycle.lifecycleScope
import com.xfhy.allinone.R
import com.xfhy.library.basekit.activity.TitleBarActivity
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import java.io.File
import java.io.FileOutputStream

/**
 * @author : xfhy
 * Create time : 2021/8/16 21:51
 * Description : 插件化
 */
class PluginAppActivity : TitleBarActivity() {

    override fun getThisTitle() = "插件化"

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_plugin_app)
    }

    fun loadPluginApk(view: View) {
        lifecycleScope.launch(Dispatchers.IO) {
            //构建一个DexClassLoader 用来加载插件包里面的class
            val inputStream = assets.open("plugin_app.apk")
            val file = File("${cacheDir}/plugin_app.apk")
            val outputStream = FileOutputStream(file)
            inputStream.copyTo(outputStream)
            //dexClassLoader = DexClassLoader(file.path, cacheDir.path, null, classLoader)

            //将插件包里面的class放入BaseDexClassLoader的DexPathList中
            InsertDexUtils.injectDexAtFirst(file.path, cacheDir.path)
        }
    }

    fun loadPluginApkNormalMethod(view: View) {
        //找插件里面的类,然后获取构造方法,构建对象
        val loadClazz = Class.forName("com.xfhy.pluginapkdemo.PluginPeople")
        val constructor = loadClazz.declaredConstructors[0]
        constructor.isAccessible = true
        val pluginPeople = constructor.newInstance()

        //调用对象的方法
        val printLogMethod = loadClazz.getDeclaredMethod("printLog")
        printLogMethod.isAccessible = true
        printLogMethod.invoke(pluginPeople)
    }

    fun startPluginApkActivity(view: View) {
        //---------------方案1 hook IActivityManager----------------
        //待打开的Activity是TargetActivity,占坑的Activity是StubActivity
        //直接启动插件里面的Activity是不行的,通不过AMS的检查,所以需要在宿主app里面弄一个占坑的Activity,然后再hook系统流程,把StubActivity拿给AMS去检查,然后在ActivityThread这边,在创建Activity时再替换成TargetActivity进行创建.
        //startActivity流程:
        // ContextImpl.startActivity->Instrumentation.execStartActivity->
        //  8.0以下 ActivityManagerNative.getDefault()->startActivity
        //  8.0以上ActivityManager.getService()->startActivity
        //  10.0以上 ActivityTaskManager.getService()->startActivity

        //10.0以下都hook IActivityManager,10.0及以上hook IActivityTaskManager,一旦是有人调用startActivity方法,那么判断一下是否在调用插件内的Activity,如果是,那么先把Intent替换成StubActivity,然后继续走
        //此时已经到AMS那边去了,AMS检查啥的完成之后,再通知ApplicationThread这边去创建该Activity,ApplicationThread->ActivityThread->H->handleMessage
        //通过hook H的mCallback,从而知道是create Activity的时机. (9.0以前message是多个,分开的case,9.0之后是一个EXECUTE_TRANSACTION,Activity生命周期都走这一个case.)
        //H收到这个消息时,将SubActivity还原成TargetActivity  即可实现偷天换日,瞒天过海

        //插件化Activity生命周期管理  https://blog.csdn.net/u011016373/article/details/82867198

        // Caused by: android.content.ActivityNotFoundException: Unable to find explicit activity class {com.xfhy.allinone/com.xfhy.pluginapkdemo.MainActivity};
        // have you declared this activity in your AndroidManifest.xml?
        val intent = Intent()
        intent.setClassName("com.xfhy.pluginapkdemo", "com.xfhy.pluginapkdemo.MainActivity")
        startActivity(intent)
        //插件包的Activity的log是xfhy_plugin

        //要调用插件包里面的Activity,还需要把class 放进DexPathList里面去  不然ActivityThread launch Activity的时候,那个classLoader找不到我插件包里面的Activity
    }
}
