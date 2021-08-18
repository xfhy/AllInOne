package com.xfhy.allinone.actual.plugin

import android.os.Bundle
import android.view.View
import com.xfhy.allinone.R
import com.xfhy.library.basekit.activity.TitleBarActivity
import dalvik.system.DexClassLoader
import java.io.File
import java.io.FileOutputStream

/**
 * @author : xfhy
 * Create time : 2021/8/16 21:51
 * Description : 实战问题
 */
class PluginAppActivity : TitleBarActivity() {

    private lateinit var dexClassLoader: DexClassLoader

    override fun getThisTitle() = "插件化"

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_plugin_app)
    }

    fun loadPluginApk(view: View) {
        val inputStream = assets.open("plugin_app.apk")
        val file = File("${cacheDir}/plugin_app.apk")
        val outputStream = FileOutputStream(file)
        inputStream.copyTo(outputStream)
        dexClassLoader = DexClassLoader(file.path, cacheDir.path, null, null)
    }

    fun loadPluginApkNormalMethod(view: View) {
        //找插件里面的类,然后获取构造方法,构建对象
        val loadClazz = dexClassLoader.loadClass("com.xfhy.pluginapkdemo.PluginPeople")
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
        //直接启动插件里面的Activity是不行的,通不过AMS的检查
        //插件化Activity生命周期管理  https://blog.csdn.net/u011016373/article/details/82867198

    }
}
